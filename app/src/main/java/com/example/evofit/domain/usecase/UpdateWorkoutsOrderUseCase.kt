package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository

class UpdateWorkoutsOrderUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workouts: List<Workout>) {
        // Atualiza os índices de ordem com base na posição na lista
        val updatedWorkouts = workouts.mapIndexed { index, workout ->
            workout.copy(orderIndex = index)
        }
        repository.updateWorkoutsOrder(updatedWorkouts)
    }
}
