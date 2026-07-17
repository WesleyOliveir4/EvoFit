package com.example.evofit.domain.usecase.profile

import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetActiveUserGoalsUseCase {
    operator fun invoke(): Flow<List<UserGoal>>
}

class GetActiveUserGoalsUseCaseImpl(
    private val onboardingRepository: OnboardingRepository
) : GetActiveUserGoalsUseCase {
    override fun invoke(): Flow<List<UserGoal>> {
        return onboardingRepository.getUserData().map { it?.goals ?: emptyList() }
    }
}
