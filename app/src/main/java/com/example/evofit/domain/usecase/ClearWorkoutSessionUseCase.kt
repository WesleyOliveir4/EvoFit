package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.WorkoutSessionRepository

interface ClearWorkoutSessionUseCase {
    suspend operator fun invoke()
}

class ClearWorkoutSessionUseCaseImpl(
    private val repository: WorkoutSessionRepository
) : ClearWorkoutSessionUseCase {
    override suspend fun invoke() {
        repository.clearSession()
    }
}