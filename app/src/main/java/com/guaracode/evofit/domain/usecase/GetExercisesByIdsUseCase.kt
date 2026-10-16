package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Exercise
import com.guaracode.evofit.domain.repository.ExerciseRepository

interface GetExercisesByIdsUseCase {
    operator fun invoke(ids: List<String>): List<Exercise>
}

class GetExercisesByIdsUseCaseImpl(
    private val repository: ExerciseRepository
) : GetExercisesByIdsUseCase {
    override fun invoke(ids: List<String>): List<Exercise> = repository.getExercisesByIds(ids)
}
