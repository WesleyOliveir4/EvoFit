package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WorkoutDone

interface GetWorkoutsCountUseCase {
    operator fun invoke(history: List<WorkoutDone>): Int
}

class GetWorkoutsCountUseCaseImpl : GetWorkoutsCountUseCase {
    override fun invoke(history: List<WorkoutDone>): Int {
        return history.size
    }
}
