package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

interface GetWorkoutByIdUseCase {
    operator fun invoke(workoutId: Long): Flow<Workout?>
}

class GetWorkoutByIdUseCaseImpl(private val repository: WorkoutRepository) : GetWorkoutByIdUseCase {
    override fun invoke(workoutId: Long): Flow<Workout?> {
        return repository.getWorkoutById(workoutId)
    }
}
