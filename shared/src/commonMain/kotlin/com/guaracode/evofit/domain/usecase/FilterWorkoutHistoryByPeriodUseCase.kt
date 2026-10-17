package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.core.common.DateMapper
import com.guaracode.evofit.domain.model.EvoPeriod
import com.guaracode.evofit.domain.model.WorkoutDone
import kotlinx.datetime.*

interface FilterWorkoutHistoryByPeriodUseCase {
    operator fun invoke(history: List<WorkoutDone>, period: EvoPeriod): List<WorkoutDone>
}

class FilterWorkoutHistoryByPeriodUseCaseImpl : FilterWorkoutHistoryByPeriodUseCase {
    override fun invoke(history: List<WorkoutDone>, period: EvoPeriod): List<WorkoutDone> {
        val startMillis = getStartMillisForPeriod(period) ?: return history

        return history.filter { workout ->
            val workoutMillis = DateMapper.parseDateUtc(workout.date)
            workoutMillis != null && workoutMillis >= startMillis
        }
    }

    private fun getStartMillisForPeriod(period: EvoPeriod): Long? {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val localDate = when (period) {
            EvoPeriod.LAST_30_DAYS -> today.minus(30, DateTimeUnit.DAY)
            EvoPeriod.LAST_90_DAYS -> today.minus(90, DateTimeUnit.DAY)
            EvoPeriod.LAST_180_DAYS -> today.minus(180, DateTimeUnit.DAY)
            EvoPeriod.ALL_TIME -> return null
        }
        return localDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
