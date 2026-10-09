package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

interface CompleteOnboardingUseCase {
    suspend operator fun invoke(data: UserOnboardingData): Result<Unit>
}

class CompleteOnboardingUseCaseImpl(
    private val repository: OnboardingRepository,
    private val authRepository: AuthRepository
) : CompleteOnboardingUseCase {
    override suspend fun invoke(data: UserOnboardingData): Result<Unit> {
        return try {
            val userId = authRepository.getCurrentUserId()
                ?: repository.getUserId().firstOrNull() 
                ?: UUID.randomUUID().toString()
            
            if (data.name.isNotBlank()) {
                authRepository.updateDisplayName(data.name)
            }

            repository.saveUserData(data, userId, isCompleted = true)
            repository.completeOnboarding()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
