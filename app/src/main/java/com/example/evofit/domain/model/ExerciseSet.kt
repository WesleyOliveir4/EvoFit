package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSet(
    val id: Long = 0,
    val workoutExerciseId: Long = 0,
    val setNumber: Int,
    val reps: Int,
    val load: Double,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val time: Int? = null,
    val distance: Double? = null
)
