package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MuscleGroupType {
    CHEST, BACK, SHOULDERS, ARMS, LEGS, ABS, CARDIO, GLUTES, CALVES, OTHER
}

@Serializable
data class MuscleGroup(
    val id: String,
    val name: String,
    val type: MuscleGroupType,
    val category: ExerciseCategory = ExerciseCategory.STRENGTH
)
