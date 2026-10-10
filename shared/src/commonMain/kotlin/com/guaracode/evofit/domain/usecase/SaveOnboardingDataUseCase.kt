package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull

interface SaveOnboardingDataUseCase {
    suspend operator fun invoke(data: UserOnboardingData): Result<Unit>
}

class SaveOnboardingDataUseCaseImpl(
    private val repository: OnboardingRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : SaveOnboardingDataUseCase {
    override suspend fun invoke(data: UserOnboardingData): Result<Unit> {
        return try {
            val userId = getUserIdUseCase().firstOrNull() ?: ""
            repository.saveUserData(data, userId, isCompleted = false)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
