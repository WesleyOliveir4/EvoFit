package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.OnboardingRepository

interface GetUserIdUseCase {
    suspend operator fun invoke(): String?
}

class GetUserIdUseCaseImpl(private val repository: OnboardingRepository) : GetUserIdUseCase {
    override suspend fun invoke(): String? {
        return repository.getUserId()
    }
}
