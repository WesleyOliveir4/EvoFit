package com.guaracode.evofit.domain.usecase

import kotlinx.datetime.*

interface GetCurrentWeekRangeUseCase {
    operator fun invoke(): Long
}

class GetCurrentWeekRangeUseCaseImpl : GetCurrentWeekRangeUseCase {
    override fun invoke(): Long {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val daysToSubtract = when (today.dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 2
            DayOfWeek.WEDNESDAY -> 3
            DayOfWeek.THURSDAY -> 4
            DayOfWeek.FRIDAY -> 5
            DayOfWeek.SATURDAY -> 6
            else -> 0
        }
        val startOfWeek = today.minus(daysToSubtract, DateTimeUnit.DAY)
        return startOfWeek.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
