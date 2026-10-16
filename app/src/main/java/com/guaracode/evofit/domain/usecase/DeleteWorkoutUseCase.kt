package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.WorkoutRepository

interface DeleteWorkoutUseCase {
    suspend operator fun invoke(workoutId: String)
}

class DeleteWorkoutUseCaseImpl(private val repository: WorkoutRepository) : DeleteWorkoutUseCase {
    override suspend fun invoke(workoutId: String) {
        repository.deleteWorkout(workoutId)
    }
}
