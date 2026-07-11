package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository

interface SaveWorkoutUseCase {
    suspend operator fun invoke(workout: Workout): Long
}

class SaveWorkoutUseCaseImpl(private val repository: WorkoutRepository) : SaveWorkoutUseCase {
    override suspend fun invoke(workout: Workout): Long {
        return repository.saveWorkout(workout)
    }
}
