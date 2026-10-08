package com.guaracode.evofit.domain.usecase.profile

import com.guaracode.evofit.domain.model.UserGoal
import com.guaracode.evofit.domain.repository.OnboardingRepository
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
