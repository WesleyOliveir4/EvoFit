package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutGroup(
    val muscleGroupId: String = "",
    val muscleGroup: MuscleGroup? = null,
    val orderIndex: Int = 0,
    val exercises: List<WorkoutExercise> = emptyList()
)
