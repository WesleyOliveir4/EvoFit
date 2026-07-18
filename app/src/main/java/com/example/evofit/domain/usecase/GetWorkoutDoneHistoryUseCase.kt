package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository

interface GetWorkoutDoneHistoryUseCase {
    suspend operator fun invoke(userId: String): List<WorkoutDone>
}

class GetWorkoutDoneHistoryUseCaseImpl(
    private val repository: WorkoutRepository
) : GetWorkoutDoneHistoryUseCase {
    override suspend fun invoke(userId: String): List<WorkoutDone> {
        return repository.getWorkoutDoneHistory(userId)
    }
}
