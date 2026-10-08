package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Exercise
import com.guaracode.evofit.domain.repository.ExerciseRepository

interface GetExercisesByGroupUseCase {
    operator fun invoke(groupId: String): List<Exercise>
}

class GetExercisesByGroupUseCaseImpl(
    private val repository: ExerciseRepository
) : GetExercisesByGroupUseCase {
    override fun invoke(groupId: String): List<Exercise> = 
        repository.getExercisesByGroup(groupId)
            .filter { it.isEnabled }
            .sortedBy { it.sortOrder }
}
