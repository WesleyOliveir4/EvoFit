package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock

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
            ?: (Clock.System.now().toEpochMilliseconds().toString() + "_" + (1000..9999).random())
            
        repository.saveUserData(data, userId, isCompleted = false)
    }
}
