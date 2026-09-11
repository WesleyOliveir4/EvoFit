package com.example.evofit.data.repository

import android.content.Context
import android.net.Uri
import com.example.evofit.data.datasource.UserLocalDataSource
import com.example.evofit.data.datasource.UserRemoteDataSource
import com.example.evofit.data.datasource.WorkoutLocalDataSource
import com.example.evofit.data.datasource.WorkoutRemoteDataSource
import com.example.evofit.data.local.entities.SyncStatus
import com.example.evofit.data.mapper.*
import com.example.evofit.data.local.session.SessionManager
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.model.WeightUpdate
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingRepositoryImpl(
    private val userDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val workoutLocalDataSource: WorkoutLocalDataSource,
    private val workoutRemoteDataSource: WorkoutRemoteDataSource,
    private val sessionManager: SessionManager,
    private val context: Context
) : OnboardingRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getUserData(): Flow<UserOnboardingData?> {
        return userDataSource.getUser().flatMapLatest { userEntity ->
            if (userEntity == null) {
                flowOf(null)
            } else {
                userDataSource.getGoalsForUser(userEntity.id).map { goals ->
                    mapToDomain(userEntity, goals)
                }
            }
        }
    }

    override fun getUserId(): Flow<String?> {
        return userDataSource.getUser().map { it?.id }
    }

    override suspend fun saveUserData(data: UserOnboardingData, userId: String, isCompleted: Boolean) {
        val userEntity = data.toEntity(userId).copy(
            onboardingCompleted = isCompleted,
            updatedAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING
        )
        val goalEntities = data.goals.map { 
            it.toEntity(userId).copy(
                updatedAt = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING,
                isDeleted = false
            )
        }
        
        // Local Write
        userDataSource.saveUserWithGoals(userEntity, goalEntities)

        triggerBackgroundSync(userId)
    }

    override suspend fun completeOnboarding() {
        userDataSource.getUser().firstOrNull()?.let { user ->
            val updatedUser = user.copy(
                onboardingCompleted = true,
                updatedAt = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING
            )
            userDataSource.updateUser(updatedUser)
            triggerBackgroundSync(user.id)
        }
    }

    override suspend fun deleteGoal(goalId: String) {
        getUserId().firstOrNull()?.let { userId ->
            userDataSource.softDeleteGoal(goalId, System.currentTimeMillis())
            triggerBackgroundSync(userId)
        }
    }

    private fun triggerBackgroundSync(userId: String) {
        scope.launch {
            try {
                pushLocalDataToRemote(userId)
            } catch (e: Exception) {
                // Background sync failure is handled by next sync cycle
            }
        }
    }

    override suspend fun deleteProfilePicture(uri: String) {
        try {
            val file = File(context.filesDir, "profile/profile_photo.jpg")
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            // Error deleting local file
        }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> {
        return userDataSource.getUser().map { it?.onboardingCompleted ?: false }
    }

    override suspend fun isOnboardingCompletedDirect(): Boolean {
        return userDataSource.getUserDirect()?.onboardingCompleted ?: false
    }

    override suspend fun syncUserData(userId: String, shouldClearActiveSession: Boolean, isOnline: Boolean): Result<Unit> {
        if (!isOnline) return Result.success(Unit)

        return try {
            // Se for um novo login (limpeza de sessão), NÃO fazemos PUSH. 
            // Queremos apenas o que está no Firebase (Cloud-Driven).
            if (!shouldClearActiveSession) {
                pushLocalDataToRemote(userId)
            }

            val lastSync = sessionManager.lastSyncTime.first()
            val isFullSync = shouldClearActiveSession || lastSync == 0L

            coroutineScope {
                // 2. PULL in parallel
                val remoteUserDeferred = async { userRemoteDataSource.getUser(userId) }
                val remoteGoalsDeferred = async { userRemoteDataSource.getGoals(userId, if (isFullSync) 0L else lastSync) }
                val remoteWorkoutsDeferred = async { workoutRemoteDataSource.getAllWorkouts(userId, if (isFullSync) 0L else lastSync) }
                val remoteNewHistoryDeferred = async { workoutRemoteDataSource.getAllWorkoutDoneHistory(userId, if (isFullSync) 0L else lastSync) }
                val remoteWeightHistoryDeferred = async { userRemoteDataSource.getWeightHistory(userId, if (isFullSync) 0L else lastSync) }
                
                // Legacy only on full sync
                val remoteLegacyHistoryDeferred = if (isFullSync) {
                    async { workoutRemoteDataSource.getWorkoutDoneHistory(userId) }
                } else null

                val remoteUser = remoteUserDeferred.await()
                val remoteGoals = remoteGoalsDeferred.await()
                val remoteWorkouts = remoteWorkoutsDeferred.await()
                val remoteNewHistory = remoteNewHistoryDeferred.await()
                val remoteWeightHistory = remoteWeightHistoryDeferred.await()
                val remoteLegacyHistory = remoteLegacyHistoryDeferred?.await()

                // 3. PERSIST to Local
                if (isFullSync) {
                    if (shouldClearActiveSession) {
                        nukeUserData()
                    }
                    userDataSource.syncAllData(
                        user = remoteUser,
                        goals = remoteGoals,
                        workouts = remoteWorkouts,
                        legacyHistory = remoteLegacyHistory,
                        newHistory = remoteNewHistory.map { it.toEntity() },
                        shouldClearActiveSession = false
                    )
                } else {
                    // Incremental Sync
                    userDataSource.syncIncremental(
                        user = remoteUser,
                        goals = remoteGoals,
                        workouts = remoteWorkouts,
                        newHistory = remoteNewHistory.map { it.toEntity() }
                    )
                }

                // Sync Weight History (incremental update)
                remoteWeightHistory.forEach { weightUpdate ->
                    if (weightUpdate.isDeleted) {
                        userDataSource.deleteWeightUpdateById(weightUpdate.id)
                    } else {
                        userDataSource.insertWeightUpdate(weightUpdate)
                    }
                }

                // 4. Update sync time
                sessionManager.updateSyncTime(System.currentTimeMillis())
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun pushLocalDataToRemote(userId: String) = coroutineScope {
        try {
            // 1. Push User
            val pendingUser = userDataSource.getPendingUser()
            pendingUser?.let {
                userRemoteDataSource.saveUser(it)
                // Após o save, o Firestore gerou um Server Timestamp. 
                // Idealmente, deveríamos ler de volta, mas para simplificar, 
                // marcamos como SYNCED com o tempo local aproximado ou 0 (confiando no próximo Pull)
                userDataSource.markUserSynced(it.id, System.currentTimeMillis())
            }

            // 2. Push Goals
            val pendingGoals = userDataSource.getPendingGoals()
            if (pendingGoals.isNotEmpty()) {
                userRemoteDataSource.saveGoals(userId, pendingGoals)
                pendingGoals.forEach { 
                    userDataSource.markGoalSynced(it.id, System.currentTimeMillis()) 
                }
            }

            // 3. Push Workouts
            val pendingWorkouts = workoutLocalDataSource.getPendingWorkouts()
            pendingWorkouts.forEach { fullWorkout ->
                val exercises = fullWorkout.exercises.map { it.workoutExercise }
                val sets = fullWorkout.exercises.map { it.sets }
                
                if (fullWorkout.workout.isDeleted) {
                    workoutRemoteDataSource.deleteWorkout(userId, fullWorkout.workout.workoutId)
                } else {
                    workoutRemoteDataSource.saveFullWorkout(fullWorkout.workout, exercises, sets)
                }
                workoutLocalDataSource.markWorkoutSynced(fullWorkout.workout.workoutId, System.currentTimeMillis())
            }

            // 4. Push New History
            val pendingHistory = workoutLocalDataSource.getPendingWorkoutDone()
            pendingHistory.forEach { entity ->
                if (entity.isDeleted) {
                    workoutRemoteDataSource.deleteWorkoutDone(userId, entity.id)
                } else {
                    workoutRemoteDataSource.saveWorkoutDone(entity.toDomain())
                }
                workoutLocalDataSource.markWorkoutDoneSynced(entity.id)
            }

            // 5. Push Weight History
            val pendingWeights = userDataSource.getPendingWeightUpdates()
            pendingWeights.forEach { entity ->
                if (entity.isDeleted) {
                    userRemoteDataSource.deleteWeightUpdate(userId, entity.id)
                } else {
                    userRemoteDataSource.saveWeightUpdate(userId, entity)
                }
                userDataSource.markWeightUpdateSynced(entity.id, System.currentTimeMillis())
            }

        } catch (e: Exception) {
            // Error pushing data
        }
    }

    override suspend fun nukeUserData() {
        userDataSource.nukeUserData()
        sessionManager.updateSyncTime(0L)
        // Limpar fotos de perfil locais (Privacidade/LGPD)
        try {
            val profileDir = File(context.filesDir, "profile")
            if (profileDir.exists()) {
                profileDir.deleteRecursively()
            }
        } catch (e: Exception) {
            // Error cleaning profile photos
        }
    }

    override suspend fun saveWeightUpdate(weightUpdate: WeightUpdate, userId: String) {
        val entity = weightUpdate.toEntity(userId).copy(
            syncStatus = SyncStatus.PENDING,
            isDeleted = false,
            timestamp = System.currentTimeMillis()
        )
        userDataSource.insertWeightUpdate(entity)
        triggerBackgroundSync(userId)
    }

    override fun getWeightHistory(userId: String): Flow<List<WeightUpdate>> {
        return userDataSource.getWeightHistory(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
