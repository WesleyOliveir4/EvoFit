package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.OnboardingRepository

interface SyncUserDataUseCase {
    suspend operator fun invoke(userId: String, shouldClearActiveSession: Boolean, isOnline: Boolean): Result<Unit>
}

class SyncUserDataUseCaseImpl(
    private val repository: OnboardingRepository
) : SyncUserDataUseCase {
    override suspend fun invoke(userId: String, shouldClearActiveSession: Boolean, isOnline: Boolean): Result<Unit> {
        return repository.syncUserData(userId, shouldClearActiveSession, isOnline)
    }
}
