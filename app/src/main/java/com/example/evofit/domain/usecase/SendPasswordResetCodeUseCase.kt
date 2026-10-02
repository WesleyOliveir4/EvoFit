package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

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
        return try {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } catch (_: Throwable) {
            emailRegex.matches(email)
        }
    }
}
