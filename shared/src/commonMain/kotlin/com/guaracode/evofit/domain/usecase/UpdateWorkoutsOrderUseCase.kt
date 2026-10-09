package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.repository.WorkoutRepository

interface UpdateWorkoutsOrderUseCase {
    suspend operator fun  invoke(workouts: List<Workout>)
}


class UpdateWorkoutsOrderUseCaseImpl(
    private val repository: WorkoutRepository
): UpdateWorkoutsOrderUseCase {
    override suspend fun invoke(workouts: List<Workout>) {
        val updatedWorkouts = workouts.mapIndexed { index, workout ->
            workout.copy(orderIndex = index)
        }
        repository.updateWorkoutsOrder(updatedWorkouts)
    }
}