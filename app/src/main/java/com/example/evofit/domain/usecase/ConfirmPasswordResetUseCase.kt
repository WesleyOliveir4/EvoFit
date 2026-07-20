package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

interface ConfirmPasswordResetUseCase {
    suspend operator fun invoke(oobCode: String, newPassword: String): Result<Unit>
}

class ConfirmPasswordResetUseCaseImpl(
    private val repository: AuthRepository
) : ConfirmPasswordResetUseCase {
    override suspend fun invoke(oobCode: String, newPassword: String): Result<Unit> {
        if (oobCode.isBlank()) {
            return Result.failure(IllegalArgumentException("Invalid reset session"))
        }
        if (newPassword.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }
        return repository.confirmPasswordReset(oobCode, newPassword)
    }
}
