package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WorkoutDone
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWorkoutsCountUseCaseTest {

    private val useCase = GetWorkoutsCountUseCaseImpl()

    @Test
    fun `should return total count of items in workout done history`() {
        val history = listOf(
            WorkoutDone(id = "1", userId = "u1"),
            WorkoutDone(id = "2", userId = "u1"),
            WorkoutDone(id = "3", userId = "u1")
        )

        val count = useCase(history)

        assertEquals(3, count)
    }
}
