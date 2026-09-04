package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone

interface GetLeastTrainedGroupUseCase {
    operator fun invoke(history: List<WorkoutDone>): Pair<String, Int>?
}

class GetLeastTrainedGroupUseCaseImpl : GetLeastTrainedGroupUseCase {
    override fun invoke(history: List<WorkoutDone>): Pair<String, Int>? {
        if (history.isEmpty()) return null

        return history
            .flatMap { it.exercisesByGroup }
            .groupBy { it.muscleGroup?.name ?: "Unknown" }
            .minByOrNull { it.value.size }
            ?.let { it.key to it.value.size }
    }
}
