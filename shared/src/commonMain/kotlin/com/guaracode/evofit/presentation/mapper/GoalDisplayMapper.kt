package com.guaracode.evofit.presentation.mapper

import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.UserGoal
import com.guaracode.evofit.presentation.model.GoalUIModel

fun UserGoal.toUiModel(): GoalUIModel {
    val categoryLabel = when (this) {
        is UserGoal.Strength -> "Força"
        is UserGoal.Cardio -> "Cardio"
        is UserGoal.Weight -> "Peso"
    }
    return GoalUIModel(
        id = id,
        categoryLabel = categoryLabel,
        displayText = getDisplayText()
    )
}

fun UserGoal.getDisplayText(): String {
    return when (this) {
        is UserGoal.Strength -> {
            when (unit) {
                MeasurementUnit.WEIGHT -> "$exerciseName: $value kg"
                MeasurementUnit.REPS -> "$exerciseName: $value reps"
                MeasurementUnit.TIME, MeasurementUnit.DISTANCE -> "$exerciseName: $value"
            }
        }
        is UserGoal.Cardio -> {
            if (!distance.isNullOrEmpty()) {
                "$type: $distance km em $time min"
            } else {
                "$type: $time min"
            }
        }
        is UserGoal.Weight -> {
            "Meta de peso: $targetWeight kg"
        }
    }
}
