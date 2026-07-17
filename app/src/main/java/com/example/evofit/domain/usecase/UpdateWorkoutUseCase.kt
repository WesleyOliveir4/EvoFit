package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository

interface UpdateWorkoutUseCase {
    suspend operator fun invoke(workout: Workout): String
}

class UpdateWorkoutUseCaseImpl(private val repository: WorkoutRepository) : UpdateWorkoutUseCase {
    override suspend fun invoke(workout: Workout): String {
        return repository.updateWorkout(workout)
    }
}
