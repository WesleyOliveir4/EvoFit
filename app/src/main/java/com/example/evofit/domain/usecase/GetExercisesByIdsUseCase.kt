package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.repository.ExerciseRepository

interface GetExercisesByIdsUseCase {
    operator fun invoke(ids: List<String>): List<Exercise>
}

class GetExercisesByIdsUseCaseImpl(
    private val repository: ExerciseRepository
) : GetExercisesByIdsUseCase {
    override fun invoke(ids: List<String>): List<Exercise> = repository.getExercisesByIds(ids)
}
