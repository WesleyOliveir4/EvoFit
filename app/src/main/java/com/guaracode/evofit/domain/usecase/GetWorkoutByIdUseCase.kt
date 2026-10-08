package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

interface GetWorkoutByIdUseCase {
    operator fun invoke(workoutId: String): Flow<Workout?>
}

class GetWorkoutByIdUseCaseImpl(private val repository: WorkoutRepository) : GetWorkoutByIdUseCase {
    override fun invoke(workoutId: String): Flow<Workout?> {
        return repository.getWorkoutById(workoutId)
    }
}
