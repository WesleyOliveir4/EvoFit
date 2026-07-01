package com.example.evofit.domain.repository

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(userId: String): Flow<List<Workout>>
    fun getWorkoutById(workoutId: Long): Flow<Workout?>
    suspend fun saveWorkout(workout: Workout): Long
    suspend fun saveWorkoutDone(userId: String, workoutDone: WorkoutDone)
}
