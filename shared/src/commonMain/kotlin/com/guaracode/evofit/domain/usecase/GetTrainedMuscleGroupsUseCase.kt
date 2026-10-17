package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.MuscleGroup
import com.guaracode.evofit.domain.model.WorkoutDone

interface GetTrainedMuscleGroupsUseCase {
    operator fun invoke(history: List<WorkoutDone>, allGroups: List<MuscleGroup>): List<MuscleGroup>
}

class GetTrainedMuscleGroupsUseCaseImpl(
    private val filterTrainedMuscleGroupsUseCase: FilterTrainedMuscleGroupsUseCase
) : GetTrainedMuscleGroupsUseCase {
    override fun invoke(history: List<WorkoutDone>, allGroups: List<MuscleGroup>): List<MuscleGroup> {
        return filterTrainedMuscleGroupsUseCase(history, allGroups)
    }
}
