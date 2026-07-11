package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.repository.WorkoutSessionRepository

interface UpdateCompletedSetsUseCase {
    suspend operator fun invoke(completedSets: List<CompletedSet>)
}

class UpdateCompletedSetsUseCaseImpl(
    private val repository: WorkoutSessionRepository
) : UpdateCompletedSetsUseCase {
    override suspend fun invoke(completedSets: List<CompletedSet>) {
        repository.updateCompletedSets(completedSets)
    }
}
