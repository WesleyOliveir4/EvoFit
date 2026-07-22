package com.example.evofit.data.repository

import com.example.evofit.data.datasource.UserLocalDataSource
import com.example.evofit.data.datasource.UserRemoteDataSource
import com.example.evofit.data.datasource.WorkoutLocalDataSource
import com.example.evofit.data.datasource.WorkoutRemoteDataSource
import com.example.evofit.data.mapper.mapToDomain
import com.example.evofit.data.mapper.toEntity
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingRepositoryImpl(
    private val userDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val workoutLocalDataSource: WorkoutLocalDataSource,
    private val workoutRemoteDataSource: WorkoutRemoteDataSource
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

    override suspend fun getUserId(): String? {
        return userDataSource.getUser().firstOrNull()?.id
    }

    override suspend fun saveUserData(data: UserOnboardingData, userId: String, isCompleted: Boolean) {
        val userEntity = data.toEntity(userId).copy(
            isOnboardingCompleted = isCompleted,
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
                isOnboardingCompleted = true,
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

    override fun isOnboardingCompleted(): Flow<Boolean> {
        return userDataSource.getUser().map { it?.isOnboardingCompleted ?: false }
    }

    override suspend fun syncUserData(userId: String): Result<Unit> {
        return try {
            // Fetch everything from Remote
            val remoteUser = userRemoteDataSource.getUser(userId)
            val remoteGoals = userRemoteDataSource.getGoals(userId)
            val remoteWorkouts = workoutRemoteDataSource.getAllWorkouts(userId)
            val remoteHistory = workoutRemoteDataSource.getWorkoutDoneHistory(userId)

            // Nuke Local
            userDataSource.nukeUserData()

            // Save to Local
            if (remoteUser != null) {
                userDataSource.saveUserWithGoals(remoteUser, remoteGoals)
            }

            if (remoteWorkouts.isNotEmpty()) {
                remoteWorkouts.forEach { data ->
                    workoutLocalDataSource.insertFullWorkout(data.workout, data.exercises, data.sets)
                }
            }

            if (remoteHistory != null) {
                workoutLocalDataSource.insertWorkoutDoneHistory(remoteHistory)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
