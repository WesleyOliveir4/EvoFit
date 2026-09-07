package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface GetWorkoutDoneByIdUseCase {
    operator fun invoke(workoutDoneId: String): Flow<WorkoutDone?>
}

class GetWorkoutDoneByIdUseCaseImpl(
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : GetWorkoutDoneByIdUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(workoutDoneId: String): Flow<WorkoutDone?> {
        return getUserIdUseCase().flatMapLatest { userId ->
            getWorkoutDoneHistoryUseCase(userId ?: "").map { history ->
                history.find { it.id == workoutDoneId }
            }
        }
    }
}
