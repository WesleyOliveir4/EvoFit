package com.example.evofit.presentation.mapper

import android.content.Context
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal

fun UserGoal.getDisplayText(context: Context): String {
    return when (this) {
        is UserGoal.Strength -> {
            when (unit) {
                MeasurementUnit.WEIGHT -> context.getString(R.string.goal_display_strength_weight, exerciseName, value)
                MeasurementUnit.REPS -> context.getString(R.string.goal_display_strength_reps, exerciseName, value)
                MeasurementUnit.TIME, MeasurementUnit.DISTANCE -> context.getString(R.string.goal_display_strength_value, exerciseName, value)
            }
        }
        is UserGoal.Cardio -> {
            if (!distance.isNullOrEmpty()) {
                context.getString(R.string.goal_display_cardio_with_distance, type, distance, time)
            } else {
                context.getString(R.string.goal_display_cardio_no_distance, type, time)
            }
        }
        is UserGoal.Weight -> {
            context.getString(R.string.goal_display_target_weight, targetWeight)
        }
    }
}
