package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWorkoutByIdUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = GetWorkoutByIdUseCaseImpl(repository)

    @Test
    fun `should delegate invoke to workoutRepository getWorkoutById`() = runBlocking {
        val workout = Workout("w123", "u1", "Treino C", "01/01/2026", emptyList(), 0)
        every { repository.getWorkoutById("w123") } returns flowOf(workout)

        val result = useCase("w123").first()

        assertEquals("Treino C", result?.name)
        verify { repository.getWorkoutById("w123") }
    }
}
