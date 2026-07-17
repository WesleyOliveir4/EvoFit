package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.WorkoutSessionRepository

interface StartWorkoutSessionUseCase {
    suspend operator fun invoke(workoutId: String, startTimeMillis: Long)
}

class StartWorkoutSessionUseCaseImpl(
    private val repository: WorkoutSessionRepository
) : StartWorkoutSessionUseCase {
    override suspend fun invoke(workoutId: String, startTimeMillis: Long) {
        repository.startSession(workoutId, startTimeMillis)
    }
}