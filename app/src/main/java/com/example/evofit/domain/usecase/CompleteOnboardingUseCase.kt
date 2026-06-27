package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import java.util.UUID

interface CompleteOnboardingUseCase {
    suspend operator fun invoke(data: UserOnboardingData)
}

class CompleteOnboardingUseCaseImpl(private val repository: OnboardingRepository) : CompleteOnboardingUseCase {
    override suspend fun invoke(data: UserOnboardingData) {
        val userId = repository.getUserId() ?: UUID.randomUUID().toString()
        repository.saveUserData(data, userId, isCompleted = true)
        repository.completeOnboarding()
    }
}
