package com.example.evofit.domain.model

sealed interface UserGoal {
    val id: String

    data class Strength(
        override val id: String,
        val exerciseName: String,
        val value: String,
        val unit: MeasurementUnit
    ) : UserGoal

    data class Cardio(
        override val id: String,
        val type: String,
        val distance: String?,
        val time: String
    ) : UserGoal

    data class Weight(
        override val id: String,
        val targetWeight: String
    ) : UserGoal
}
