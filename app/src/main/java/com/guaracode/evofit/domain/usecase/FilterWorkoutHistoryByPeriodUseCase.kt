package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.core.common.DateMapper
import com.guaracode.evofit.domain.model.EvoPeriod
import com.guaracode.evofit.domain.model.WorkoutDone
import java.util.Calendar
import java.util.Date

/**
 * Regra de negócio "o que conta como dentro do período selecionado" — antes vivia
 * dentro do WorkoutRepositoryImpl, o que misturava regra de negócio com acesso a dados.
 */
interface FilterWorkoutHistoryByPeriodUseCase {
    operator fun invoke(history: List<WorkoutDone>, period: EvoPeriod): List<WorkoutDone>
}

class FilterWorkoutHistoryByPeriodUseCaseImpl : FilterWorkoutHistoryByPeriodUseCase {
    override fun invoke(history: List<WorkoutDone>, period: EvoPeriod): List<WorkoutDone> {
        val startDate = getStartDateForPeriod(period) ?: return history

        return history.filter { workout ->
            val workoutDate = DateMapper.parseDate(workout.date)
            workoutDate != null && workoutDate.after(startDate)
        }
    }

    private fun getStartDateForPeriod(period: EvoPeriod): Date? {
        val calendar = Calendar.getInstance()
        return when (period) {
            EvoPeriod.LAST_30_DAYS -> {
                calendar.add(Calendar.MONTH, -1)
                calendar.time
            }
            EvoPeriod.LAST_90_DAYS -> {
                calendar.add(Calendar.MONTH, -3)
                calendar.time
            }
            EvoPeriod.LAST_180_DAYS -> {
                calendar.add(Calendar.MONTH, -6)
                calendar.time
            }
            EvoPeriod.ALL_TIME -> null
        }
    }
}
