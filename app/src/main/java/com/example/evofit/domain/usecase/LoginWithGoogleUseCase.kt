package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

interface LoginWithGoogleUseCase {
    suspend operator fun invoke(idToken: String): Result<Unit>
}

class LoginWithGoogleUseCaseImpl(
    private val repository: AuthRepository
) : LoginWithGoogleUseCase {
    override suspend fun invoke(idToken: String): Result<Unit> {
        if (idToken.isBlank()) {
            return Result.failure(IllegalArgumentException("ID Token cannot be empty"))
        }
        return repository.loginWithGoogle(idToken)
    }
}
