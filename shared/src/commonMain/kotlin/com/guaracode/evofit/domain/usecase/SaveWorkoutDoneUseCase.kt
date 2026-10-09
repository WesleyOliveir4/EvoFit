package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.repository.WorkoutRepository

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
