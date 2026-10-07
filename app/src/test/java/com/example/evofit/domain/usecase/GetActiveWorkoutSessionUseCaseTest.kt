package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutSession
import com.example.evofit.domain.repository.WorkoutRepository
import com.example.evofit.domain.repository.WorkoutSessionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GetActiveWorkoutSessionUseCaseTest {

    private val sessionRepository: WorkoutSessionRepository = mockk(relaxed = true)
    private val workoutRepository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = GetActiveWorkoutSessionUseCaseImpl(sessionRepository, workoutRepository)

    @Test
    fun `when active session exists, should map to ActiveWorkoutSession`() = runBlocking {
        val session = WorkoutSession("w10", 1000L, listOf(CompletedSet("we1", 1)))
        val workout = Workout("w10", "u1", "Treino Ativo", "date", emptyList(), 0)

        every { sessionRepository.getActiveSession() } returns flowOf(session)
        every { workoutRepository.getWorkoutById("w10") } returns flowOf(workout)

        val result = useCase().first()

        assertNotNull(result)
        assertEquals("Treino Ativo", result?.workout?.name)
        assertEquals(1000L, result?.startTime)
        assertEquals(1, result?.completedSets?.size)
    }

    @Test
    fun `when active session is null, should emit null`() = runBlocking {
        every { sessionRepository.getActiveSession() } returns flowOf(null)

        val result = useCase().first()

        assertNull(result)
    }
}
