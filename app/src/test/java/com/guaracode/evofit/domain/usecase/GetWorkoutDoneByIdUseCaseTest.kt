package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WorkoutDone
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GetWorkoutDoneByIdUseCaseTest {

    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase = mockk(relaxed = true)
    private val getUserIdUseCase: GetUserIdUseCase = mockk(relaxed = true)
    private val useCase = GetWorkoutDoneByIdUseCaseImpl(getWorkoutDoneHistoryUseCase, getUserIdUseCase)

    @Test
    fun `when workoutDone exists in history, should return it`() = runBlocking {
        val wd1 = WorkoutDone(id = "target_id", userId = "u1", name = "Treino Alvo")
        val wd2 = WorkoutDone(id = "other_id", userId = "u1", name = "Outro Treino")

        every { getUserIdUseCase() } returns flowOf("u1")
        every { getWorkoutDoneHistoryUseCase("u1") } returns flowOf(listOf(wd1, wd2))

        val result = useCase("target_id").first()

        assertNotNull(result)
        assertEquals("Treino Alvo", result?.name)
    }

    @Test
    fun `when workoutDone does not exist in history, should return null`() = runBlocking {
        every { getUserIdUseCase() } returns flowOf("u1")
        every { getWorkoutDoneHistoryUseCase("u1") } returns flowOf(emptyList())

        val result = useCase("non_existent").first()

        assertNull(result)
    }
}
