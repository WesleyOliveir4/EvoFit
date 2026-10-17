package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.core.common.DateMapper
import com.guaracode.evofit.domain.model.WorkoutDone
import kotlinx.datetime.*

interface GetKmPerWeekUseCase {
    operator fun invoke(history: List<WorkoutDone>): Double
}

class GetKmPerWeekUseCaseImpl : GetKmPerWeekUseCase {
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
            val millis = DateMapper.parseDateUtc(workout.date)
            if (millis != null) {
                Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault()).date
            } else null
        }.sorted()

        if (dates.isEmpty()) return 0.0
        
        val firstDate = dates.first()
        val lastDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        val days = (lastDate.toEpochDays() - firstDate.toEpochDays()).toLong().coerceAtLeast(1)
        val weeks = days / 7.0
        
        return if (weeks > 0) totalKm / weeks else totalKm
    }
}
