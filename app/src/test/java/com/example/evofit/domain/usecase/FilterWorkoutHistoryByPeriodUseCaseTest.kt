package com.example.evofit.domain.usecase

import com.example.evofit.core.common.DateMapper
import com.example.evofit.domain.model.EvoPeriod
import com.example.evofit.domain.model.WorkoutDone
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class FilterWorkoutHistoryByPeriodUseCaseTest {

    private val useCase = FilterWorkoutHistoryByPeriodUseCaseImpl()

    @Test
    fun `when period is ALL_TIME, should return all history items`() {
        val history = listOf(WorkoutDone(id = "1", date = "01/01/2020"))

        val result = useCase(history, EvoPeriod.ALL_TIME)

        assertEquals(1, result.size)
    }

    @Test
    fun `when period is LAST_30_DAYS, should filter out older workouts`() {
        val calendar = Calendar.getInstance()
        val todayStr = DateMapper.formatDate(calendar.time)

        calendar.add(Calendar.MONTH, -2)
        val oldDateStr = DateMapper.formatDate(calendar.time)

        val history = listOf(
            WorkoutDone(id = "recent", date = todayStr),
            WorkoutDone(id = "old", date = oldDateStr)
        )

        val result = useCase(history, EvoPeriod.LAST_30_DAYS)

        assertEquals(1, result.size)
        assertEquals("recent", result.first().id)
    }
}
