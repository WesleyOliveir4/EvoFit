package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WeightUpdate
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

interface GetWeightHistoryUseCase {
    operator fun invoke(userId: String): Flow<List<WeightUpdate>>
}

class GetWeightHistoryUseCaseImpl(
    private val repository: OnboardingRepository
) : GetWeightHistoryUseCase {
    override fun invoke(userId: String): Flow<List<WeightUpdate>> {
        return repository.getWeightHistory(userId)
    }
}
