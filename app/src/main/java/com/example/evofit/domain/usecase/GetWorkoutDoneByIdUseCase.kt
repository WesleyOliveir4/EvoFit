package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone

interface GetWorkoutDoneByIdUseCase {
    suspend operator fun invoke(workoutDoneId: Long): WorkoutDone?
}

class GetWorkoutDoneByIdUseCaseImpl(
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : GetWorkoutDoneByIdUseCase {
    override suspend fun invoke(workoutDoneId: Long): WorkoutDone? {
        val userId = getUserIdUseCase() ?: return null
        return getWorkoutDoneHistoryUseCase(userId).find { it.id == workoutDoneId }
    }
}
