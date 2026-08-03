package com.example.evofit.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.evofit.data.datasource.UserLocalDataSource
import com.example.evofit.data.datasource.UserRemoteDataSource
import com.example.evofit.data.datasource.WorkoutLocalDataSource
import com.example.evofit.data.datasource.WorkoutRemoteDataSource
import com.example.evofit.data.mapper.*
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingRepositoryImpl(
    private val userDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val workoutLocalDataSource: WorkoutLocalDataSource,
    private val workoutRemoteDataSource: WorkoutRemoteDataSource,
    private val context: Context
) : OnboardingRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getUserData(): Flow<UserOnboardingData?> {
        return userDataSource.getUser().flatMapLatest { userEntity ->
            if (userEntity == null) {
                Log.d("OnboardingRepo", "UserEntity é null no local")
                flowOf(null)
            } else {
                Log.d("OnboardingRepo", "UserEntity local: birthDate=${userEntity.birthDate}")
                userDataSource.getGoalsForUser(userEntity.id).map { goals ->
                    mapToDomain(userEntity, goals)
                }
            }
        }.onEach { Log.d("OnboardingRepo", "UserOnboardingData emitido: birthDate=${it?.birthDate}") }
    }

    override suspend fun getUserId(): String? {
        return userDataSource.getUser().firstOrNull()?.id
    }

    override suspend fun saveUserData(data: UserOnboardingData, userId: String, isCompleted: Boolean) {
        val userEntity = data.toEntity(userId).copy(
            onboardingCompleted = isCompleted,
            updatedAt = System.currentTimeMillis()
        )
        val goalEntities = data.goals.map { it.toEntity(userId) }
        
        // Local Write
        userDataSource.saveUserWithGoals(userEntity, goalEntities)

        // Remote Write (Fire and forget for now, keeping it simple)
        scope.launch {
            try {
                userRemoteDataSource.saveUser(userEntity)
                userRemoteDataSource.saveGoals(userId, goalEntities)
            } catch (e: Exception) {
                // Log error or handle retry logic if needed
            }
        }
    }

    override suspend fun completeOnboarding() {
        userDataSource.getUser().firstOrNull()?.let { user ->
            val updatedUser = user.copy(
                onboardingCompleted = true,
                updatedAt = System.currentTimeMillis()
            )
            userDataSource.updateUser(updatedUser)
            
            scope.launch {
                try {
                    userRemoteDataSource.saveUser(updatedUser)
                } catch (e: Exception) {
                }
            }
        }
    }

    override suspend fun deleteGoal(goalId: String) {
        val userId = getUserId()
        userDataSource.deleteGoalById(goalId)
        
        if (userId != null) {
            scope.launch {
                try {
                    userRemoteDataSource.deleteGoal(userId, goalId)
                } catch (e: Exception) {
                }
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
            Log.e("OnboardingRepo", "Erro ao deletar foto de perfil", e)
        }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> {
        return userDataSource.getUser().map { it?.onboardingCompleted ?: false }
    }

    override suspend fun syncUserData(userId: String, shouldClearActiveSession: Boolean, isOnline: Boolean): Result<Unit> {
        if (!isOnline) return Result.success(Unit) // Skip if offline

        return try {
            // 1. PUSH: Upload local data created/modified offline
            pushLocalDataToRemote(userId)

            // 2. PULL: Fetch everything from Remote
            val remoteUser = userRemoteDataSource.getUser(userId)
            val remoteGoals = userRemoteDataSource.getGoals(userId)
            val remoteWorkouts = workoutRemoteDataSource.getAllWorkouts(userId)
            val remoteLegacyHistory = workoutRemoteDataSource.getWorkoutDoneHistory(userId)
            val remoteNewHistory = workoutRemoteDataSource.getAllWorkoutDoneHistory(userId)

            // 3. UPDATE LOCAL ATOMICALLY: Nuke/Clear + Save
            if (shouldClearActiveSession) {
                nukeUserData() // Isso agora limpa as fotos de perfil locais também
            }

            userDataSource.syncAllData(
                user = remoteUser,
                goals = remoteGoals,
                workouts = remoteWorkouts,
                legacyHistory = remoteLegacyHistory,
                newHistory = remoteNewHistory.map { it.toEntity() },
                shouldClearActiveSession = false // Já limpamos acima
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun pushLocalDataToRemote(userId: String) {
        try {
            // Push Workouts
            val localWorkouts = workoutLocalDataSource.getFullWorkouts(userId).firstOrNull() ?: emptyList()
            localWorkouts.forEach { fullWorkout ->
                val exercises = fullWorkout.exercises.map { it.workoutExercise }
                val sets = fullWorkout.exercises.map { it.sets }
                workoutRemoteDataSource.saveFullWorkout(fullWorkout.workout, exercises, sets)
            }

            // Push Legacy History
            val localLegacyHistory = workoutLocalDataSource.getWorkoutDoneHistory(userId).firstOrNull()
            if (localLegacyHistory != null) {
                workoutRemoteDataSource.saveWorkoutDoneHistory(localLegacyHistory)
            }

            // Push New History
            val localNewHistory = workoutLocalDataSource.getAllWorkoutDoneHistory(userId).firstOrNull() ?: emptyList()
            localNewHistory.forEach { entity ->
                workoutRemoteDataSource.saveWorkoutDone(entity.toDomain())
            }
        } catch (e: Exception) {
            // Log error, push might fail but we continue to pull
        }
    }

    override suspend fun nukeUserData() {
        userDataSource.nukeUserData()
        // Limpar fotos de perfil locais (Privacidade/LGPD)
        try {
            val profileDir = File(context.filesDir, "profile")
            if (profileDir.exists()) {
                profileDir.deleteRecursively()
            }
        } catch (e: Exception) {
            Log.e("OnboardingRepo", "Erro ao limpar fotos de perfil no nuke", e)
        }
    }
}
