package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.OnboardingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class IsOnboardingCompletedUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val useCase = IsOnboardingCompletedUseCaseImpl(repository)

    @Test
    fun `invoke should emit onboarding completed boolean flow`() = runBlocking {
        every { repository.isOnboardingCompleted() } returns flowOf(true)

        val result = useCase().first()

        assertTrue(result)
        verify { repository.isOnboardingCompleted() }
    }

    @Test
    fun `executeDirect should return onboarding completed boolean directly`() = runBlocking {
        coEvery { repository.isOnboardingCompletedDirect() } returns true

        val result = useCase.executeDirect()

        assertTrue(result)
        coVerify { repository.isOnboardingCompletedDirect() }
    }
}
