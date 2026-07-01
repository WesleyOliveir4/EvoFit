package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

interface GetWorkoutsUseCase {
    operator fun invoke(userId: String): Flow<List<Workout>>
}

class GetWorkoutsUseCaseImpl(private val repository: WorkoutRepository) : GetWorkoutsUseCase {
    override fun invoke(userId: String): Flow<List<Workout>> {
        return repository.getWorkouts(userId)
    }
}
