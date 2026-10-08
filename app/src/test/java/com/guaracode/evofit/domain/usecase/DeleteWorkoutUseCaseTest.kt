package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.WorkoutRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DeleteWorkoutUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = DeleteWorkoutUseCaseImpl(repository)

    @Test
    fun `should delegate deleteWorkout to repository`() = runBlocking {
        useCase("w123")

        coVerify { repository.deleteWorkout("w123") }
    }
}
