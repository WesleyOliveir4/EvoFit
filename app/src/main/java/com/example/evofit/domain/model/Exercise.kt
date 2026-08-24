package com.example.evofit.domain.model

data class Exercise(
    val id: String,
    val name: String,
    val muscleGroupId: String,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val sortOrder: Int = 0,
    val isEnabled: Boolean = true
)
