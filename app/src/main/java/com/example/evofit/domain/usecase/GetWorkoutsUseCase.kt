package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutsUseCase(private val repository: WorkoutRepository) {
    operator fun invoke(userId: String): Flow<List<Workout>> {
        return repository.getWorkouts(userId)
    }
}
