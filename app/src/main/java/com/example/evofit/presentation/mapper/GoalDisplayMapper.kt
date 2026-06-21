package com.example.evofit.presentation.mapper

import android.content.Context
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal

fun UserGoal.getDisplayText(context: Context): String {
    return when (this) {
        is UserGoal.Strength -> {
            when (unit) {
                MeasurementUnit.WEIGHT -> "$exerciseName - ${value}kg"
                MeasurementUnit.REPS -> "$exerciseName - $value reps"
                MeasurementUnit.TIME -> "$exerciseName - $value"
                MeasurementUnit.DISTANCE -> "$exerciseName - $value"
            }
        }
        is UserGoal.Cardio -> {
            if (!distance.isNullOrEmpty()) "$type - ${distance}km em $time" else "$type - $time"
        }
        is UserGoal.Weight -> {
            "Meta de Peso: ${targetWeight}kg"
        }
    }
}
