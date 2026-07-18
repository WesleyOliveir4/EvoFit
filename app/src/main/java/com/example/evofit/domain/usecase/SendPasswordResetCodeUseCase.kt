package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

interface SendPasswordResetCodeUseCase {
    suspend operator fun invoke(email: String): Result<Unit>
}

class SendPasswordResetCodeUseCaseImpl(
    private val repository: AuthRepository
) : SendPasswordResetCodeUseCase {
    override suspend fun invoke(email: String): Result<Unit> {
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(IllegalArgumentException("Invalid email address"))
        }
        return repository.sendPasswordResetCode(email)
    }
}
