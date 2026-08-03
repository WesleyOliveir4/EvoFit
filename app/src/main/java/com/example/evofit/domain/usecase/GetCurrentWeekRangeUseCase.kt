package com.example.evofit.domain.usecase

import java.util.Calendar

interface GetCurrentWeekRangeUseCase {
    operator fun invoke(): Long
}

class GetCurrentWeekRangeUseCaseImpl : GetCurrentWeekRangeUseCase {
    override fun invoke(): Long {
        val calendar = Calendar.getInstance()
        
        // Zerar as horas para comparação correta
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Retroceder até o domingo mais recente (ou hoje se for domingo)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return calendar.timeInMillis
    }
}
