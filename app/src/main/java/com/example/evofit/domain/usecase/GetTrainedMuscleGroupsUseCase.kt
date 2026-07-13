package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.MuscleGroup

interface GetTrainedMuscleGroupsUseCase {
    suspend operator fun invoke(): List<MuscleGroup>
}

class GetTrainedMuscleGroupsUseCaseImpl(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val filterTrainedMuscleGroupsUseCase: FilterTrainedMuscleGroupsUseCase
) : GetTrainedMuscleGroupsUseCase {
    override suspend fun invoke(): List<MuscleGroup> {
        val userId = getUserIdUseCase() ?: return emptyList()
        val history = getWorkoutDoneHistoryUseCase(userId)
        return filterTrainedMuscleGroupsUseCase(history)
    }
}
