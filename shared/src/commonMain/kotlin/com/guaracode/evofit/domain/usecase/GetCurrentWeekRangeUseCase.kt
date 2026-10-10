package com.guaracode.evofit.domain.usecase

import kotlinx.datetime.*

interface GetCurrentWeekRangeUseCase {
    operator fun invoke(): Long
}

class GetCurrentWeekRangeUseCaseImpl : GetCurrentWeekRangeUseCase {
    override fun invoke(): Long {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val daysToSubtract = today.dayOfWeek.isoDayNumber - 1
        val monday = today.minus(daysToSubtract, DateTimeUnit.DAY)
        return monday.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
