package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.AuthRepository

interface SendPasswordResetCodeUseCase {
    suspend operator fun invoke(email: String): Result<Unit>
}

class SendPasswordResetCodeUseCaseImpl(
    private val repository: AuthRepository
) : SendPasswordResetCodeUseCase {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$")

    override suspend fun invoke(email: String): Result<Unit> {
        if (email.isBlank() || !isValidEmail(email)) {
            return Result.failure(IllegalArgumentException("Invalid email address"))
        }
        return repository.sendPasswordResetCode(email)
    }

    private fun isValidEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }
}
