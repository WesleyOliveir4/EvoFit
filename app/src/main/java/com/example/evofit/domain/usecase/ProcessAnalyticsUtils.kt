package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import java.util.Locale

internal fun groupWorkoutsByMonth(workouts: List<WorkoutDone>): Map<String, List<WorkoutDone>> {
    return workouts.groupBy { workout ->
        try {
            if (workout.date.contains("/")) {
                val parts = workout.date.split("/")
                "${parts[1]}/${parts[2]}"
            } else {
                workout.date.substring(0, 7)
            }
        } catch (e: Exception) {
            workout.date
        }
    }
}

internal fun formatSecondsToTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}

internal fun formatDateToMonth(dateStr: String): String {
    return try {
        val month = if (dateStr.contains("-")) {
            dateStr.split("-")[1]
        } else {
            dateStr.split("/")[1]
        }
        when (month) {
            "01" -> "Jan"
            "02" -> "Fev"
            "03" -> "Mar"
            "04" -> "Abr"
            "05" -> "Mai"
            "06" -> "Jun"
            "07" -> "Jul"
            "08" -> "Ago"
            "09" -> "Set"
            "10" -> "Out"
            "11" -> "Nov"
            "12" -> "Dez"
            else -> ""
        }
    } catch (e: Exception) {
        ""
    }
}

internal fun formatDate(dateStr: String): String {
    return try {
        if (dateStr.contains("-")) {
            val parts = dateStr.split("-")
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } else {
            dateStr
        }
    } catch (e: Exception) {
        dateStr
    }
}
