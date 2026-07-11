package com.example.evofit.domain.repository

import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.model.WorkoutSession
import kotlinx.coroutines.flow.Flow

interface WorkoutSessionRepository {
    fun getActiveSession(): Flow<WorkoutSession?>
    suspend fun startSession(workoutId: Long, startTimeMillis: Long)
    suspend fun updateCompletedSets(completedSets: List<CompletedSet>)
    suspend fun clearSession()
}
