package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.repository.WorkoutRepository
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
