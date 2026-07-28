package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDone(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val muscleGroupId: String = "",
    val muscleGroup: MuscleGroup? = null,
    val date: String = "",
    val exercises: List<WorkoutExercise> = emptyList(),
    val time: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class WorkoutDoneHistory(
    val userId: String = "",
    val history: List<WorkoutDone> = emptyList()
)
