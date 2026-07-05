package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExercise(
    val id: Long = 0,
    val exerciseId: String,
    val sets: List<ExerciseSet>
)
