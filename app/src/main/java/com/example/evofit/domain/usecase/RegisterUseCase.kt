package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

interface RegisterUseCase {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit>
}

class RegisterUseCaseImpl(
    private val repository: AuthRepository
) : RegisterUseCase {
    override suspend fun invoke(name: String, email: String, password: String): Result<Unit> {
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Name cannot be empty"))
        }
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }
        return repository.register(name, email, password)
    }
}
