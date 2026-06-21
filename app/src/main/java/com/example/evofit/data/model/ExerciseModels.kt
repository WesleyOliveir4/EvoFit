package com.example.evofit.data.model

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MeasurementUnit

data class MuscleGroupModel(
    val id: String,
    val name: String,
    val category: ExerciseCategory = ExerciseCategory.STRENGTH
)

data class ExerciseModel(
    val id: String,
    val name: String,
    val muscleGroupId: String,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT
)

data class MuscleGroupWithExercises(
    val muscleGroup: MuscleGroupModel,
    val exercises: List<ExerciseModel>
)
