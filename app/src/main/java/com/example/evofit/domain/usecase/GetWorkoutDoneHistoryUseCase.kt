package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository

class GetWorkoutDoneHistoryUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(userId: String): List<WorkoutDone> {
        return repository.getWorkoutDoneHistory(userId)
    }
}
