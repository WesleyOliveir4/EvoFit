package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository

interface SaveWorkoutDoneUseCase {
    suspend operator fun invoke(userId: String, workoutDone: WorkoutDone)
}

class SaveWorkoutDoneUseCaseImpl(
    private val repository: WorkoutRepository
) : SaveWorkoutDoneUseCase {
    override suspend fun invoke(userId: String, workoutDone: WorkoutDone) {
        repository.saveWorkoutDone(userId, workoutDone)
    }
}
