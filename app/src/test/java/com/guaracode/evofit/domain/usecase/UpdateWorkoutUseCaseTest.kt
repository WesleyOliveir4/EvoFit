package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.repository.WorkoutRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdateWorkoutUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = UpdateWorkoutUseCaseImpl(repository)

    @Test
    fun `should delegate updateWorkout to repository`() = runBlocking {
        val workout = Workout("w1", "u1", "Treino A Atualizado", "01/01/2026", emptyList(), 0)
        coEvery { repository.updateWorkout(workout) } returns "w1"

        val result = useCase(workout)

        assertEquals("w1", result)
        coVerify { repository.updateWorkout(workout) }
    }
}
