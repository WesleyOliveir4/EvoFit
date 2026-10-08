package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.repository.WorkoutRepository

interface UpdateWorkoutUseCase {
    suspend operator fun invoke(workout: Workout): String
}

class UpdateWorkoutUseCaseImpl(private val repository: WorkoutRepository) : UpdateWorkoutUseCase {
    override suspend fun invoke(workout: Workout): String {
        return repository.updateWorkout(workout)
    }
}
