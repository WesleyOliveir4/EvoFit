package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.AuthRepository

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
