package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

interface GetWorkoutsUseCase {
    operator fun invoke(userId: String): Flow<List<Workout>>
}

class GetWorkoutsUseCaseImpl(private val repository: WorkoutRepository) : GetWorkoutsUseCase {
    override fun invoke(userId: String): Flow<List<Workout>> {
        return repository.getWorkouts(userId)
    }
}
