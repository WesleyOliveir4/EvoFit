package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

interface GetWorkoutDoneHistoryUseCase {
    operator fun invoke(userId: String, limit: Int = 100): Flow<List<WorkoutDone>>
}

class GetWorkoutDoneHistoryUseCaseImpl(
    private val repository: WorkoutRepository
) : GetWorkoutDoneHistoryUseCase {
    override fun invoke(userId: String, limit: Int): Flow<List<WorkoutDone>> {
        return repository.getWorkoutDoneHistory(userId, limit)
    }
}
