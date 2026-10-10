package com.guaracode.evofit.presentation.mapper

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
        is UserGoal.Strength -> "$exerciseName: $value"
        is UserGoal.Cardio -> if (!distance.isNullOrEmpty()) "$type ($distance km em $time)" else "$type ($time)"
        is UserGoal.Weight -> "Meta: $targetWeight kg"
    }
}
