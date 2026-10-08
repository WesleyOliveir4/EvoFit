package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.AuthRepository

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
