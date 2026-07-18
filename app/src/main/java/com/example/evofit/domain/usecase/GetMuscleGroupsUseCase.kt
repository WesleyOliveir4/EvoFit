package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.repository.ExerciseRepository

interface GetMuscleGroupsUseCase {
    operator fun invoke(): List<MuscleGroup>
}

class GetMuscleGroupsUseCaseImpl(
    private val repository: ExerciseRepository
) : GetMuscleGroupsUseCase {
    override fun invoke(): List<MuscleGroup> = repository.getMuscleGroups()
}
