package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

interface GetWorkoutsSinceUseCase {
    operator fun invoke(userId: String, sinceTimestamp: Long): Flow<List<WorkoutDone>>
}

class GetWorkoutsSinceUseCaseImpl(
    private val repository: WorkoutRepository
) : GetWorkoutsSinceUseCase {
    override fun invoke(userId: String, sinceTimestamp: Long): Flow<List<WorkoutDone>> {
        return repository.getWorkoutDoneSince(userId, sinceTimestamp)
    }
}
