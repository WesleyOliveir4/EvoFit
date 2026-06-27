package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetOnboardingDataUseCase {
    operator fun invoke(): Flow<UserOnboardingData>
}

class GetOnboardingDataUseCaseImpl(private val repository: OnboardingRepository) : GetOnboardingDataUseCase {
    override fun invoke(): Flow<UserOnboardingData> {
        return repository.getUserData().map { it ?: UserOnboardingData.empty() }
    }
}