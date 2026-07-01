package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository

class SaveWorkoutDoneUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(userId: String, workoutDone: WorkoutDone) {
        repository.saveWorkoutDone(userId, workoutDone)
    }
}
