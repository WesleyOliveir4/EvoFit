package com.example.evofit.data.model

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MeasurementUnit

enum class MuscleGroupType {
    CHEST, BACK, SHOULDERS, ARMS, LEGS, ABS, CARDIO, GLUTES, CALVES, OTHER
}

data class MuscleGroupModel(
    val id: String,
    val name: String,
    val type: MuscleGroupType,
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
