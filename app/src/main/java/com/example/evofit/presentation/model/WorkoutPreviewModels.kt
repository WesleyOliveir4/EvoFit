package com.example.evofit.presentation.model

import com.example.evofit.domain.model.MeasurementUnit

data class ExercisePreviewItem(
    val workoutExerciseId: Long,
    val name: String,
    val setsCount: Int,
    val weight: Double,
    val reps: Int,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val time: Int? = null,
    val distance: Double? = null
)

data class WorkoutDetailPreview(
    val title: String,
    val totalExercises: Int,
    val totalSets: Int,
    val exercises: List<ExercisePreviewItem>
)
