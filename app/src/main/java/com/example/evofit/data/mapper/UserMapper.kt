package com.example.evofit.data.mapper

import com.example.evofit.data.local.entities.UserEntity
import com.example.evofit.data.local.entities.UserGoalEntity
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import java.util.UUID

fun UserOnboardingData.toEntity(userId: String = UUID.randomUUID().toString()): UserEntity {
    return UserEntity(
        id = userId,
        name = name,
        age = age,
        weight = weight,
        height = height,
        isOnboardingCompleted = false
    )
}

fun UserGoal.toEntity(userId: String): UserGoalEntity {
    return when (this) {
        is UserGoal.Strength -> UserGoalEntity(
            id = id,
            userId = userId,
            type = "STRENGTH",
            exerciseName = exerciseName,
            value = value,
            unit = unit.name,
            cardioType = null,
            distance = null,
            time = null
        )
        is UserGoal.Cardio -> UserGoalEntity(
            id = id,
            userId = userId,
            type = "CARDIO",
            exerciseName = null,
            value = null,
            unit = null,
            cardioType = type,
            distance = distance,
            time = time
        )
        is UserGoal.Weight -> UserGoalEntity(
            id = id,
            userId = userId,
            type = "WEIGHT",
            exerciseName = null,
            value = targetWeight,
            unit = null,
            cardioType = null,
            distance = null,
            time = null
        )
    }
}

fun mapToDomain(user: UserEntity, goals: List<UserGoalEntity>): UserOnboardingData {
    return UserOnboardingData(
        name = user.name,
        age = user.age,
        weight = user.weight,
        height = user.height,
        goals = goals.map { it.toDomain() }
    )
}

fun UserGoalEntity.toDomain(): UserGoal {
    return when (type) {
        "STRENGTH" -> UserGoal.Strength(
            id = id,
            exerciseName = exerciseName ?: "",
            value = value ?: "",
            unit = unit?.let { MeasurementUnit.valueOf(it) } ?: MeasurementUnit.WEIGHT
        )
        "CARDIO" -> UserGoal.Cardio(
            id = id,
            type = cardioType ?: "",
            distance = distance,
            time = time ?: ""
        )
        "WEIGHT" -> UserGoal.Weight(
            id = id,
            targetWeight = value ?: ""
        )
        else -> throw IllegalArgumentException("Unknown goal type: $type")
    }
}
