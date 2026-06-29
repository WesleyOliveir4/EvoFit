package com.example.evofit.domain.repository

import com.example.evofit.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(userId: String): Flow<List<Workout>>
}
