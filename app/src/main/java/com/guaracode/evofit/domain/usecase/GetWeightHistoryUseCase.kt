package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WeightUpdate
import com.guaracode.evofit.domain.repository.OnboardingRepository
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
