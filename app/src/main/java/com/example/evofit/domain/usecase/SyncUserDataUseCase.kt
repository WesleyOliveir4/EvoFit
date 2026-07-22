package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.OnboardingRepository

interface SyncUserDataUseCase {
    suspend operator fun invoke(userId: String): Result<Unit>
}

class SyncUserDataUseCaseImpl(
    private val repository: OnboardingRepository
) : SyncUserDataUseCase {
    override suspend fun invoke(userId: String): Result<Unit> {
        return repository.syncUserData(userId)
    }
}
