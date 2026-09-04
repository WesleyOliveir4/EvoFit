package com.example.evofit.domain.model

data class Workout(
    val id: String = "",
    val userId: String,
    val name: String,
    val date: String,
    val exercisesByGroup: List<WorkoutGroup>,
    val orderIndex: Int = 0
)
