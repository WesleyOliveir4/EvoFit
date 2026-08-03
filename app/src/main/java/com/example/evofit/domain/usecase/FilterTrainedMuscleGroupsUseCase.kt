package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.WorkoutDone

interface FilterTrainedMuscleGroupsUseCase {
    operator fun invoke(history: List<WorkoutDone>, allGroups: List<MuscleGroup>): List<MuscleGroup>
}

class FilterTrainedMuscleGroupsUseCaseImpl : FilterTrainedMuscleGroupsUseCase {
    override fun invoke(history: List<WorkoutDone>, allGroups: List<MuscleGroup>): List<MuscleGroup> {
        val trainedGroupIds = history.map { it.muscleGroupId }.toSet()
        return allGroups.filter { trainedGroupIds.contains(it.id) }
    }
}
