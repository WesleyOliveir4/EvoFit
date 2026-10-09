package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.AuthRepository

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
