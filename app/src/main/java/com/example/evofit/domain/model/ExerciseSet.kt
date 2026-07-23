package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSet(
    val id: String = "",
    val exerciseName: String = "",
    val workoutExerciseId: String = "",
    val setNumber: Int = 0,
    val reps: Int = 0,
    val load: Double = 0.0,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val time: Int? = null,
    val distance: Double? = null
)
