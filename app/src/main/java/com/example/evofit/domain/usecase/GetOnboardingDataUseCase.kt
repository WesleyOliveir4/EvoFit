package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class GetOnboardingDataUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Flow<UserOnboardingData> = repository.getUserData()
}
