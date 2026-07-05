package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ActiveWorkoutSession
import com.example.evofit.domain.repository.WorkoutRepository
import com.example.evofit.domain.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.first

class GetActiveWorkoutSessionUseCase(
    private val sessionRepository: WorkoutSessionRepository,
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(): ActiveWorkoutSession? {
        val session = sessionRepository.getActiveSession() ?: return null
        val workout = workoutRepository.getWorkoutById(session.workoutId).first() ?: return null
        return ActiveWorkoutSession(
            workout = workout,
            startTime = session.startTime,
            completedSets = session.completedSets
        )
    }
}
