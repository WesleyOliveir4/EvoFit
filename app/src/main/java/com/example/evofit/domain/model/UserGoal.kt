package com.example.evofit.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed interface UserGoal {
    val id: String
    val title: String

    @Serializable
    data class Strength(
        override val id: String = UUID.randomUUID().toString(),
        val exerciseName: String,
        val value: String,
        val unit: MeasurementUnit,
        override val title: String = when (unit) {
            MeasurementUnit.WEIGHT -> "$exerciseName - ${value}kg"
            MeasurementUnit.REPS -> "$exerciseName - $value reps"
            MeasurementUnit.TIME -> "$exerciseName - $value"
            else -> "$exerciseName - $value"
        }
    ) : UserGoal

    @Serializable
    data class Cardio(
        override val id: String = UUID.randomUUID().toString(),
        val type: String,
        val distance: String? = null,
        val time: String,
        override val title: String = if (!distance.isNullOrEmpty()) "$type - ${distance}km em $time" else "$type - $time"
    ) : UserGoal

    @Serializable
    data class Weight(
        override val id: String = UUID.randomUUID().toString(),
        val targetWeight: String,
        override val title: String = "Meta de Peso: ${targetWeight}kg"
    ) : UserGoal
}
