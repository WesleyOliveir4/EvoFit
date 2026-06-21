package com.example.evofit.data.model

import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class UserGoalDto {
    abstract val id: String

    @Serializable
    @SerialName("strength")
    data class StrengthDto(
        override val id: String,
        val exerciseName: String,
        val value: String,
        val unit: MeasurementUnit
    ) : UserGoalDto()

    @Serializable
    @SerialName("cardio")
    data class CardioDto(
        override val id: String,
        val type: String,
        val distance: String?,
        val time: String
    ) : UserGoalDto()

    @Serializable
    @SerialName("weight")
    data class WeightDto(
        override val id: String,
        val targetWeight: String
    ) : UserGoalDto()
}

fun UserGoal.toDto(): UserGoalDto = when (this) {
    is UserGoal.Strength -> UserGoalDto.StrengthDto(id, exerciseName, value, unit)
    is UserGoal.Cardio -> UserGoalDto.CardioDto(id, type, distance, time)
    is UserGoal.Weight -> UserGoalDto.WeightDto(id, targetWeight)
}

fun UserGoalDto.toDomain(): UserGoal = when (this) {
    is UserGoalDto.StrengthDto -> UserGoal.Strength(id, exerciseName, value, unit)
    is UserGoalDto.CardioDto -> UserGoal.Cardio(id, type, distance, time)
    is UserGoalDto.WeightDto -> UserGoal.Weight(id, targetWeight)
}
