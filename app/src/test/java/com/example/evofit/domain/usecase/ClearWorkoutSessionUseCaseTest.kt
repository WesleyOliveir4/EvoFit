package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.WorkoutSessionRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ClearWorkoutSessionUseCaseTest {

    private val repository: WorkoutSessionRepository = mockk(relaxed = true)
    private val useCase = ClearWorkoutSessionUseCaseImpl(repository)

    @Test
    fun `should delegate clearSession to repository`() = runBlocking {
        useCase()

        coVerify { repository.clearSession() }
    }
}
