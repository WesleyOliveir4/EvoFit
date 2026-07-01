package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

interface IsOnboardingCompletedUseCase {
    operator fun invoke(): Flow<Boolean>
}

class IsOnboardingCompletedUseCaseImpl(private val repository: OnboardingRepository) : IsOnboardingCompletedUseCase {
    override fun invoke(): Flow<Boolean> {
        return repository.isOnboardingCompleted()
    }
}
