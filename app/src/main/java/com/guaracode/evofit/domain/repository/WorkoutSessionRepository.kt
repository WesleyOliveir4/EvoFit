package com.guaracode.evofit.domain.repository

import com.guaracode.evofit.domain.model.CompletedSet
import com.guaracode.evofit.domain.model.WorkoutSession
import kotlinx.coroutines.flow.Flow

interface WorkoutSessionRepository {
    fun getActiveSession(): Flow<WorkoutSession?>
    suspend fun startSession(workoutId: String, startTimeMillis: Long)
    suspend fun updateCompletedSets(completedSets: List<CompletedSet>)
    suspend fun clearSession()
}
