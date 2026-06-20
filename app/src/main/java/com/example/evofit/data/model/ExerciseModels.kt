package com.example.evofit.data.model

import com.example.evofit.domain.model.ExerciseCategory

data class MuscleGroupModel(
    val id: String,
    val name: String,
    val category: ExerciseCategory = ExerciseCategory.STRENGTH
)

data class ExerciseModel(
    val id: String,
    val name: String,
    val muscleGroupId: String
)

data class MuscleGroupWithExercises(
    val muscleGroup: MuscleGroupModel,
    val exercises: List<ExerciseModel>
)
