package com.example.evofit.domain.repository

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(userId: String): Flow<List<Workout>>
    fun getWorkoutById(workoutId: String): Flow<Workout?>
    suspend fun saveWorkout(workout: Workout): String
    suspend fun updateWorkout(workout: Workout): String
    suspend fun deleteWorkout(workoutId: String)
    suspend fun saveWorkoutDone(userId: String, workoutDone: WorkoutDone)
    fun getWorkoutDoneHistory(userId: String): Flow<List<WorkoutDone>>
    suspend fun updateWorkoutsOrder(workouts: List<Workout>)
    suspend fun getMaxOrderIndex(userId: String): Int
}
