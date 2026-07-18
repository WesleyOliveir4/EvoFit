package com.example.evofit.domain.model

data class Workout(
    val id: String = "",
    val userId: String,
    val name: String,
    val muscleGroupId: String,
    val muscleGroup: MuscleGroup? = null,
    val date: String,
    val exercises: List<WorkoutExercise>,
    val orderIndex: Int = 0
)
