package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository

class SaveWorkoutUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(workout: Workout): Long {
        return repository.saveWorkout(workout)
    }
}
