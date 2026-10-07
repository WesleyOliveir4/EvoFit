package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWorkoutDoneHistoryUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = GetWorkoutDoneHistoryUseCaseImpl(repository)

    @Test
    fun `should delegate getWorkoutDoneHistory to repository with limit`() = runBlocking {
        val history = listOf(WorkoutDone(id = "wd1", userId = "u1", name = "Treino Histórico"))
        every { repository.getWorkoutDoneHistory("u1", 50) } returns flowOf(history)

        val result = useCase("u1", 50).first()

        assertEquals(1, result.size)
        assertEquals("Treino Histórico", result.first().name)
        verify { repository.getWorkoutDoneHistory("u1", 50) }
    }
}
