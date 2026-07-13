package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone

interface GetWorkoutsCountUseCase {
    operator fun invoke(history: List<WorkoutDone>): Int
}

class GetWorkoutsCountUseCaseImpl : GetWorkoutsCountUseCase {
    override fun invoke(history: List<WorkoutDone>): Int {
        return history.size
    }
}
