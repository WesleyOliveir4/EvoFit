package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.WorkoutDone

interface FilterTrainedMuscleGroupsUseCase {
    operator fun invoke(history: List<WorkoutDone>): List<MuscleGroup>
}

class FilterTrainedMuscleGroupsUseCaseImpl : FilterTrainedMuscleGroupsUseCase {
    override fun invoke(history: List<WorkoutDone>): List<MuscleGroup> {
        return history.mapNotNull { it.muscleGroup }
            .distinctBy { it.id }
    }
}
