package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.AuthRepository
import com.example.evofit.domain.repository.OnboardingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface CompleteOnboardingUseCase {
    suspend operator fun invoke(data: UserOnboardingData)
}

class CompleteOnboardingUseCaseImpl(
    private val repository: OnboardingRepository,
    private val firebaseAuth: FirebaseAuth
) : CompleteOnboardingUseCase {
    override suspend fun invoke(data: UserOnboardingData) {
        val userId = repository.getUserId() ?: UUID.randomUUID().toString()
        
        // Sincroniza o nome com o perfil do Firebase Auth se o usuário estiver logado
        firebaseAuth.currentUser?.let { user ->
            if (data.name.isNotBlank()) {
                try {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(data.name)
                        .build()
                    user.updateProfile(profileUpdates).await()
                } catch (e: Exception) {
                    // Log error or handle if critical, but onboarding continues
                }
            }
        }

        repository.saveUserData(data, userId, isCompleted = true)
        repository.completeOnboarding()
    }
}
