package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.OnboardingRepository

interface NukeUserDataUseCase {
    suspend operator fun invoke()
}

class NukeUserDataUseCaseImpl(
    private val repository: OnboardingRepository
) : NukeUserDataUseCase {
    override suspend fun invoke() {
        repository.nukeUserData()
    }
}
