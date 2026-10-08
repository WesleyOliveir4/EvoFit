package com.guaracode.evofit.domain.usecase

import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class GetCurrentWeekRangeUseCaseTest {

    private val useCase = GetCurrentWeekRangeUseCaseImpl()

    @Test
    fun `should return timestamp representing start of current week sunday`() {
        val timestamp = useCase()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        assertTrue(timestamp <= System.currentTimeMillis())
        assertTrue(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        assertTrue(calendar.get(Calendar.HOUR_OF_DAY) == 0)
        assertTrue(calendar.get(Calendar.MINUTE) == 0)
    }
}
