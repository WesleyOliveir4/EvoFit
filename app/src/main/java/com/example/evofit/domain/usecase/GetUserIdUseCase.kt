package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

interface GetUserIdUseCase {
    operator fun invoke(): Flow<String?>
}

class GetUserIdUseCaseImpl(private val repository: OnboardingRepository) : GetUserIdUseCase {
    override fun invoke(): Flow<String?> {
        return repository.getUserId()
    }
}
