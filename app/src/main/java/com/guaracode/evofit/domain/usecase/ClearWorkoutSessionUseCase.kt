package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.WorkoutSessionRepository

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