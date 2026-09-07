package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.AuthRepository
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

interface SaveOnboardingDataUseCase {
    suspend operator fun invoke(data: UserOnboardingData)
}

class SaveOnboardingDataUseCaseImpl(
    private val repository: OnboardingRepository,
    private val authRepository: AuthRepository
) : SaveOnboardingDataUseCase {
    override suspend fun invoke(data: UserOnboardingData) {
        val userId = authRepository.getCurrentUserId() 
            ?: repository.getUserId().firstOrNull() 
            ?: UUID.randomUUID().toString()
            
        repository.saveUserData(data, userId, isCompleted = false)
    }
}
