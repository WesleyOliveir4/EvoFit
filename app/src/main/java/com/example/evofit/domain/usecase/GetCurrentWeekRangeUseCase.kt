package com.example.evofit.domain.usecase

import java.util.Calendar

interface GetCurrentWeekRangeUseCase {
    operator fun invoke(): Long
}

class GetCurrentWeekRangeUseCaseImpl : GetCurrentWeekRangeUseCase {
    override fun invoke(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
