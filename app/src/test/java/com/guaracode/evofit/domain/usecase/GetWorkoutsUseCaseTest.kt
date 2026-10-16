package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.repository.WorkoutRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWorkoutsUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = GetWorkoutsUseCaseImpl(repository)

    @Test
    fun `should delegate invoke to workoutRepository getWorkouts`() = runBlocking {
        val workouts = listOf(Workout("w1", "u1", "Treino A", "01/01/2026", emptyList(), 0))
        every { repository.getWorkouts("u1") } returns flowOf(workouts)

        val result = useCase("u1").first()

        assertEquals(1, result.size)
        assertEquals("Treino A", result.first().name)
        verify { repository.getWorkouts("u1") }
    }
}
