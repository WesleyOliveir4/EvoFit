package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.repository.WorkoutRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveWorkoutUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = SaveWorkoutUseCaseImpl(repository)

    @Test
    fun `should delegate saveWorkout to repository and return generated ID`() = runBlocking {
        val workout = Workout("w1", "u1", "Treino A", "01/01/2026", emptyList(), 0)
        coEvery { repository.saveWorkout(workout) } returns "generated_w1"

        val result = useCase(workout)

        assertEquals("generated_w1", result)
        coVerify { repository.saveWorkout(workout) }
    }
}
