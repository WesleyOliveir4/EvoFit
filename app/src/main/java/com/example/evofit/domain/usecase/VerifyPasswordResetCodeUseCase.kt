package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

interface VerifyPasswordResetCodeUseCase {
    suspend operator fun invoke(email: String, code: String): Result<String>
}

class VerifyPasswordResetCodeUseCaseImpl(
    private val repository: AuthRepository
) : VerifyPasswordResetCodeUseCase {
    override suspend fun invoke(email: String, code: String): Result<String> {
        if (email.isBlank() || code.length != 6) {
            return Result.failure(IllegalArgumentException("Invalid email or verification code"))
        }
        return repository.verifyPasswordResetCode(email, code)
    }
}
