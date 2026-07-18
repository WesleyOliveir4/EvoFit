package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Unit>
}

class LoginUseCaseImpl(
    private val repository: AuthRepository
) : LoginUseCase {
    override suspend fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty"))
        }
        return repository.login(email, password)
    }
}
