package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import java.util.UUID

interface SaveOnboardingDataUseCase {
    suspend operator fun invoke(data: UserOnboardingData)
}

class SaveOnboardingDataUseCaseImpl(private val repository: OnboardingRepository) : SaveOnboardingDataUseCase {
    override suspend fun invoke(data: UserOnboardingData) {
        val userId = repository.getUserId() ?: UUID.randomUUID().toString()
        repository.saveUserData(data, userId, isCompleted = false)
    }
}
