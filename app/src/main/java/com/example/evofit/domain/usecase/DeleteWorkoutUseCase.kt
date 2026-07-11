package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.WorkoutRepository

interface DeleteWorkoutUseCase {
    suspend operator fun invoke(workoutId: Long)
}

class DeleteWorkoutUseCaseImpl(private val repository: WorkoutRepository) : DeleteWorkoutUseCase {
    override suspend fun invoke(workoutId: Long) {
        repository.deleteWorkout(workoutId)
    }
}
