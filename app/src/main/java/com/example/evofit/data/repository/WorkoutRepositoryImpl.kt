package com.example.evofit.data.repository

import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.mapper.toDomain
import com.example.evofit.data.mapper.toEntity
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl(
    private val userDao: UserDao,
    private val exerciseDataSource: LocalExerciseDataSource
) : WorkoutRepository {
    override fun getWorkouts(userId: String): Flow<List<Workout>> {
        return userDao.getFullWorkouts(userId).map { fullWorkouts ->
            val muscleGroups = exerciseDataSource.getAllMuscleGroups()
            fullWorkouts.map { fullWorkout ->
                val group = muscleGroups.find { it.id == fullWorkout.workout.muscleGroupId }
                fullWorkout.toDomain(group)
            }
        }
    }

    override fun getWorkoutById(workoutId: Long): Flow<Workout?> {
        return userDao.getFullWorkoutById(workoutId).map { fullWorkout ->
            fullWorkout?.let {
                val group = exerciseDataSource.getAllMuscleGroups()
                    .find { it.id == fullWorkout.workout.muscleGroupId }
                fullWorkout.toDomain(group)
            }
        }
    }

    override suspend fun saveWorkout(workout: Workout): Long {
        val nextOrderIndex = (userDao.getMaxOrderIndex(workout.userId) ?: -1) + 1
        val workoutEntity = workout.toEntity().copy(orderIndex = nextOrderIndex)
        
        val exercises = workout.exercises.map { it.toEntity(0) }
        val sets = workout.exercises.map { exercise ->
            exercise.sets.map { it.toEntity(0) }
        }

        userDao.insertFullWorkout(workoutEntity, exercises, sets)
        return 0 // Since we're using a transaction for multiple inserts, returning 0 or the last ID. 
                 // Note: If the specific ID is needed, we'd need to return it from insertFullWorkout.
    }

    override suspend fun updateWorkoutsOrder(workouts: List<Workout>) {
        userDao.updateWorkoutsOrder(workouts.map { it.toEntity() })
    }

    override suspend fun getMaxOrderIndex(userId: String): Int {
        return userDao.getMaxOrderIndex(userId) ?: -1
    }

    override suspend fun saveWorkoutDone(userId: String, workoutDone: WorkoutDone) {
        val existingHistory = userDao.getWorkoutDoneHistory(userId)
        
        val updatedList = if (existingHistory != null) {
            existingHistory.history + workoutDone
        } else {
            listOf(workoutDone)
        }
        
        userDao.insertWorkoutDoneHistory(
            WorkoutDoneHistoryEntity(
                userId = userId,
                history = updatedList
            )
        )
    }
}
