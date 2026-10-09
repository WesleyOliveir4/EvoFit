package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.EvoPeriod
import com.guaracode.evofit.domain.model.WorkoutDone

interface FilterWorkoutHistoryByPeriodUseCase {
    operator fun invoke(history: List<WorkoutDone>, period: EvoPeriod): List<WorkoutDone>
}

class FilterWorkoutHistoryByPeriodUseCaseImpl : FilterWorkoutHistoryByPeriodUseCase {
    override fun invoke(history: List<WorkoutDone>, period: EvoPeriod): List<WorkoutDone> {
        val millisForPeriod = getMillisForPeriod(period) ?: return history
        val cutoffTime = currentTimeMillis() - millisForPeriod

        return history.filter { workout ->
            workout.createdAt >= cutoffTime
        }
    }

    private fun getMillisForPeriod(period: EvoPeriod): Long? {
        val dayMillis = 24L * 60 * 60 * 1000
        return when (period) {
            EvoPeriod.LAST_30_DAYS -> 30 * dayMillis
            EvoPeriod.LAST_90_DAYS -> 90 * dayMillis
            EvoPeriod.LAST_180_DAYS -> 180 * dayMillis
            EvoPeriod.ALL_TIME -> null
        }
    }

    private fun currentTimeMillis(): Long {
        return kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
    }
}
