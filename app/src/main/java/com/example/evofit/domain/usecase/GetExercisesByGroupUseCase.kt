package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.repository.ExerciseRepository

interface GetExercisesByGroupUseCase {
    operator fun invoke(groupId: String): List<Exercise>
}

class GetExercisesByGroupUseCaseImpl(
    private val repository: ExerciseRepository
) : GetExercisesByGroupUseCase {
    override fun invoke(groupId: String): List<Exercise> = 
        repository.getExercisesByGroup(groupId).sortedBy { it.sortOrder }
}
