package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository

class SaveOnboardingDataUseCase(private val repository: OnboardingRepository) {
    suspend operator fun invoke(data: UserOnboardingData) {
        repository.saveUserData(data)
    }
}
