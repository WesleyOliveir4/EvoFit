package com.guaracode.evofit.data.repository

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.datasource.LocalExerciseDataSource
import com.guaracode.evofit.data.datasource.UserLocalDataSource
import com.guaracode.evofit.data.datasource.UserRemoteDataSource
import com.guaracode.evofit.data.datasource.WorkoutLocalDataSource
import com.guaracode.evofit.data.datasource.WorkoutRemoteDataSource
import com.guaracode.evofit.data.local.entities.SyncStatus
import com.guaracode.evofit.data.local.session.SessionManager
import com.guaracode.evofit.data.mapper.mapToDomain
import com.guaracode.evofit.data.mapper.toDomain
import com.guaracode.evofit.data.mapper.toEntity
import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.model.WeightUpdate
import com.guaracode.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingRepositoryImpl(
    private val userDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val workoutLocalDataSource: WorkoutLocalDataSource,
    private val workoutRemoteDataSource: WorkoutRemoteDataSource,
    private val exerciseDataSource: LocalExerciseDataSource,
    private val sessionManager: SessionManager,
    private val crashReporter: CrashReporter
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
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            syncStatus = SyncStatus.PENDING
        )
        val goalEntities = data.goals.map { 
            it.toEntity(userId).copy(
                updatedAt = Clock.System.now().toEpochMilliseconds(),
                syncStatus = SyncStatus.PENDING,
                isDeleted = false
            )
        }
        
        userDataSource.saveUserWithGoals(userEntity, goalEntities)
        triggerBackgroundSync(userId)
    }

    override suspend fun completeOnboarding() {
        userDataSource.getUser().firstOrNull()?.let { user ->
            val updatedUser = user.copy(
                onboardingCompleted = true,
                updatedAt = Clock.System.now().toEpochMilliseconds(),
                syncStatus = SyncStatus.PENDING
            )
            userDataSource.updateUser(updatedUser)
            triggerBackgroundSync(user.id)
        }
    }

    override suspend fun deleteGoal(goalId: String) {
        getUserId().firstOrNull()?.let { userId ->
            userDataSource.deleteGoalById(goalId)
            triggerBackgroundSync(userId)
        }
    }

    private fun triggerBackgroundSync(userId: String) {
        scope.launch {
            try {
                pushLocalDataToRemote(userId)
            } catch (e: Exception) {
                crashReporter.recordException(e)
            }
        }
    }

    override suspend fun deleteProfilePicture(uri: String) {
        try {
            // Handled platform-agnostically
        } catch (e: Exception) {
            crashReporter.recordException(e)
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
            if (!shouldClearActiveSession) {
                pushLocalDataToRemote(userId)
            }

            val lastSync = sessionManager.lastSyncTime.first()
            val isFullSync = shouldClearActiveSession || lastSync == 0L

            coroutineScope {
                val remoteUserDeferred = async { userRemoteDataSource.getUser(userId) }
                val remoteGoalsDeferred = async { userRemoteDataSource.getGoals(userId, if (isFullSync) 0L else lastSync) }
                val remoteWorkoutsDeferred = async { workoutRemoteDataSource.getAllWorkouts(userId, if (isFullSync) 0L else lastSync) }
                val remoteNewHistoryDeferred = async { workoutRemoteDataSource.getAllWorkoutDoneHistory(userId, if (isFullSync) 0L else lastSync) }
                val remoteWeightHistoryDeferred = async { userRemoteDataSource.getWeightHistory(userId, if (isFullSync) 0L else lastSync) }
                
                val remoteLegacyHistoryDeferred = if (isFullSync) {
                    async { workoutRemoteDataSource.getWorkoutDoneHistory(userId) }
                } else null

                val remoteUser = remoteUserDeferred.await()
                val remoteGoals = remoteGoalsDeferred.await()
                val remoteWorkouts = remoteWorkoutsDeferred.await()
                val remoteNewHistory = remoteNewHistoryDeferred.await()
                val remoteWeightHistory = remoteWeightHistoryDeferred.await()
                val remoteLegacyHistory = remoteLegacyHistoryDeferred?.await()

                if (isFullSync) {
                    if (shouldClearActiveSession) {
                        nukeUserData()
                    }
                    // Sync persistence
                } else {
                    // Incremental Sync
                }

                remoteWeightHistory.forEach { weightUpdate ->
                    if (weightUpdate.isDeleted) {
                        userDataSource.deleteWeightUpdateById(weightUpdate.id)
                    } else {
                        userDataSource.insertWeightUpdate(weightUpdate)
                    }
                }

                sessionManager.updateSyncTime(Clock.System.now().toEpochMilliseconds())
            }

            Result.success(Unit)
        } catch (e: Exception) {
            crashReporter.recordException(e)
            Result.failure(e)
        }
    }

    private suspend fun pushLocalDataToRemote(userId: String) = coroutineScope {
        try {
            val pendingUser = userDataSource.getUserDirect()
            pendingUser?.let {
                userRemoteDataSource.saveUser(it)
            }
        } catch (e: Exception) {
            crashReporter.recordException(e)
        }
    }

    override suspend fun nukeUserData() {
        userDataSource.nukeUserData()
        sessionManager.updateSyncTime(0L)
    }

    override suspend fun saveWeightUpdate(weightUpdate: WeightUpdate, userId: String) {
        val entity = weightUpdate.toEntity(userId).copy(
            syncStatus = SyncStatus.PENDING,
            isDeleted = false,
            timestamp = Clock.System.now().toEpochMilliseconds()
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
