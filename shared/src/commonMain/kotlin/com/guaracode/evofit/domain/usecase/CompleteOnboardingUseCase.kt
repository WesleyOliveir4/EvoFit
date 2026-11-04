package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.core.common.randomUuid
import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.repository.OnboardingRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.firstOrNull

interface CompleteOnboardingUseCase {
    suspend operator fun invoke(data: UserOnboardingData): Result<Unit>
}

class CompleteOnboardingUseCaseImpl(
    private val repository: OnboardingRepository,
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) : CompleteOnboardingUseCase {
    override suspend fun invoke(data: UserOnboardingData): Result<Unit> {
        return try {
            val userId = authRepository.getCurrentUserId()
                ?: repository.getUserId().firstOrNull() 
                ?: randomUuid()
            
            firebaseAuth.currentUser?.let { user ->
                if (data.name.isNotBlank()) {
                    try {
                        user.updateProfile(displayName = data.name)
                    } catch (e: Exception) {
                        // Log error
                    }
                }
            }

            repository.saveUserData(data, userId, isCompleted = true)
            repository.completeOnboarding()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
