package com.example.evofit.domain.repository

import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.model.WorkoutSession

interface WorkoutSessionRepository {
    fun getActiveSession(): WorkoutSession?
    fun startSession(workoutId: Long, startTimeMillis: Long)
    fun updateCompletedSets(completedSets: List<CompletedSet>)
    fun clearSession()
}
