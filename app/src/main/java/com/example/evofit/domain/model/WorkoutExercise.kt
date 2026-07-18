package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExercise(
    val id: String = "",
    val exerciseId: String,
    val sets: List<ExerciseSet>,
    val totalSets: Int = 0
)
