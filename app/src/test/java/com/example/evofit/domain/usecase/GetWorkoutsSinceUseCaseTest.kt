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

class GetWorkoutsSinceUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = GetWorkoutsSinceUseCaseImpl(repository)

    @Test
    fun `should delegate invoke to workoutRepository getWorkoutDoneSince`() = runBlocking {
        val history = listOf(WorkoutDone(id = "wd1", userId = "u1", name = "Treino B"))
        every { repository.getWorkoutDoneSince("u1", 1000L) } returns flowOf(history)

        val result = useCase("u1", 1000L).first()

        assertEquals(1, result.size)
        assertEquals("Treino B", result.first().name)
        verify { repository.getWorkoutDoneSince("u1", 1000L) }
    }
}
