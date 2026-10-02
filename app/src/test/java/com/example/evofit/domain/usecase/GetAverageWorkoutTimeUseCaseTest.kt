package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAverageWorkoutTimeUseCaseTest {

    private val useCase = GetAverageWorkoutTimeUseCaseImpl()

    @Test
    fun `when history is empty, should return 0`() {
        assertEquals(0, useCase(emptyList()))
    }

    @Test
    fun `should parse hh mm ss time strings and calculate average in minutes`() {
        val history = listOf(
            WorkoutDone(id = "1", time = "00:30:00"),
            WorkoutDone(id = "2", time = "01:00:00")
        )

        // 30 min + 60 min = 90 min / 2 = 45 min
        val avgTime = useCase(history)

        assertEquals(45, avgTime)
    }
}
