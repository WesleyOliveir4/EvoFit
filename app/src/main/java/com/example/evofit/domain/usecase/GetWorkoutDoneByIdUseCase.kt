package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository

class GetWorkoutDoneByIdUseCase(
    private val repository: WorkoutRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) {
    suspend operator fun invoke(workoutDoneId: Long): WorkoutDone? {
        val userId = getUserIdUseCase() ?: return null
        return repository.getWorkoutDoneHistory(userId).find { it.id == workoutDoneId }
    }
}
