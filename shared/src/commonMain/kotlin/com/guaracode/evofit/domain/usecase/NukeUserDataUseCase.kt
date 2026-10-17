package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.OnboardingRepository

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
