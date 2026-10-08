package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.MuscleGroup
import com.guaracode.evofit.domain.repository.ExerciseRepository

interface GetMuscleGroupsUseCase {
    operator fun invoke(): List<MuscleGroup>
}

class GetMuscleGroupsUseCaseImpl(
    private val repository: ExerciseRepository
) : GetMuscleGroupsUseCase {
    override fun invoke(): List<MuscleGroup> = repository.getMuscleGroups()
}
