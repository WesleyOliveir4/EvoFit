package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

interface LoginWithAppleUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class LoginWithAppleUseCaseImpl(
    private val repository: AuthRepository
) : LoginWithAppleUseCase {
    override suspend fun invoke(): Result<Unit> {
        return repository.loginWithApple()
    }
}
