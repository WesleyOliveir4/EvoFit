package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.WorkoutDone
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Locale

interface GetKmPerWeekUseCase {
    operator fun invoke(history: List<WorkoutDone>): Double
}

class GetKmPerWeekUseCaseImpl : GetKmPerWeekUseCase {
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun invoke(history: List<WorkoutDone>): Double {
        if (history.isEmpty()) return 0.0

        val totalKm = history.sumOf { workout ->
            workout.exercisesByGroup.filter { it.muscleGroup?.category == ExerciseCategory.CARDIO }
                .sumOf { group ->
                    group.exercises.sumOf { exercise ->
                        exercise.sets.sumOf { it.distance ?: 0.0 }
                    }
                }
        }

        val dates = history.mapNotNull { workout ->
            parseLocalDate(workout.date)
        }.sorted()

        if (dates.isEmpty()) return 0.0
        
        val firstDate = dates.first()
        val lastDate = LocalDate.now()
        
        val days = ChronoUnit.DAYS.between(firstDate, lastDate).coerceAtLeast(1)
        val weeks = days / 7.0
        
        return if (weeks > 0) totalKm / weeks else totalKm
    }

    //TODO rever
    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseLocalDate(dateStr: String): LocalDate? {
        if (dateStr.isBlank()) return null
        val cleanDate = dateStr.trim().take(10)
        
        // Try dd/MM/yyyy
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US)
            return LocalDate.parse(cleanDate, formatter)
        } catch (e: Exception) {}

        // Try yyyy-MM-dd
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
            return LocalDate.parse(cleanDate, formatter)
        } catch (e: Exception) {}

        // Try dd-MM-yyyy
        try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US)
            return LocalDate.parse(cleanDate, formatter)
        } catch (e: Exception) {}

        return null
    }
}
