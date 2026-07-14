package com.example.evofit.domain.usecase.profile

import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetActiveUserGoalsUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(): Flow<List<UserGoal>> {
        return onboardingRepository.getUserData().map { it?.goals ?: emptyList() }
    }
}
