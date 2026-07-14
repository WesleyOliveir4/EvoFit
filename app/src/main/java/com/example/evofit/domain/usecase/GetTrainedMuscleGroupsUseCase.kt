package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.WorkoutDone

interface GetTrainedMuscleGroupsUseCase {
    operator fun invoke(history: List<WorkoutDone>): List<MuscleGroup>
}

class GetTrainedMuscleGroupsUseCaseImpl(
    private val filterTrainedMuscleGroupsUseCase: FilterTrainedMuscleGroupsUseCase
) : GetTrainedMuscleGroupsUseCase {
    override fun invoke(history: List<WorkoutDone>): List<MuscleGroup> {
        return filterTrainedMuscleGroupsUseCase(history)
    }
}
