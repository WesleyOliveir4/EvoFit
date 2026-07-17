package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository

interface IsUserLoggedInUseCase {
    operator fun invoke(): Boolean
}

class IsUserLoggedInUseCaseImpl(
    private val authRepository: AuthRepository
) : IsUserLoggedInUseCase {
    override fun invoke(): Boolean = authRepository.isLoggedIn()
}
