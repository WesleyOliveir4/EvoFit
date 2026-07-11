package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository

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