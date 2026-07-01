package com.example.evofit.data.repository

import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.mapper.toDomain
import com.example.evofit.data.mapper.toEntity
import com.example.evofit.domain.model.Workout
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
        val workoutId = userDao.insertWorkout(workout.toEntity())
        workout.exercises.forEach { exercise ->
            val exerciseId = userDao.insertWorkoutExercise(exercise.toEntity(workoutId))
            userDao.insertExerciseSets(exercise.sets.map { it.toEntity(exerciseId) })
        }
        return workoutId
    }
}
