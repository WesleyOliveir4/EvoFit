package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.WorkoutSessionRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class StartWorkoutSessionUseCaseTest {

    private val repository: WorkoutSessionRepository = mockk(relaxed = true)
    private val useCase = StartWorkoutSessionUseCaseImpl(repository)

    @Test
    fun `should delegate startSession to repository`() = runBlocking {
        useCase("w123", 5000L)

        coVerify { repository.startSession("w123", 5000L) }
    }
}
