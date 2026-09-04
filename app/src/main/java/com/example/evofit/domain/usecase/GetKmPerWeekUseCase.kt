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

        val totalKm = history
            .flatMap { it.exercisesByGroup }
            .flatMap { it.exercises }
            .flatMap { it.sets }
            .mapNotNull { it.distance }
            .sum()

        if (totalKm <= 0) return 0.0

        val dates = history.mapNotNull { workout ->
            parseLocalDate(workout.date)
        }.sorted()

        if (dates.isEmpty()) return 0.0
        
        val firstDate = dates.first()
        val lastDate = LocalDate.now()
        
        // Se a data de hoje for antes da primeira data (erro de sistema), usa pelo menos 1 dia
        val days = ChronoUnit.DAYS.between(firstDate, lastDate).coerceAtLeast(1)
        val weeks = days / 7.0
        
        // Retorna a média semanal. Se o período for < 1 semana, weeks será < 1.0, 
        // o que dará a projeção da semana se o ritmo continuar.
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
