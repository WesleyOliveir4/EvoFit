package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.repository.WorkoutRepository

interface SaveWorkoutUseCase {
    suspend operator fun invoke(workout: Workout): String
}

class SaveWorkoutUseCaseImpl(private val repository: WorkoutRepository) : SaveWorkoutUseCase {
    override suspend fun invoke(workout: Workout): String {
        return repository.saveWorkout(workout)
    }
}
