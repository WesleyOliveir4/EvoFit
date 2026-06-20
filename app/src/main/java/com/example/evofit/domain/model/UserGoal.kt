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
        val weight: String,
        override val title: String = "$exerciseName - ${weight}kg"
    ) : UserGoal

    @Serializable
    data class Cardio(
        override val id: String = UUID.randomUUID().toString(),
        val type: String,
        val distance: String,
        val time: String,
        override val title: String = "$type - ${distance}km em $time"
    ) : UserGoal

    @Serializable
    data class Weight(
        override val id: String = UUID.randomUUID().toString(),
        val targetWeight: String,
        override val title: String = "Meta de Peso: ${targetWeight}kg"
    ) : UserGoal
}
