package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.repository.OnboardingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SaveOnboardingDataUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val useCase = SaveOnboardingDataUseCaseImpl(repository, authRepository)

    @Test
    fun `should use userId from authRepository if present and save user data`() = runBlocking {
        every { authRepository.getCurrentUserId() } returns "user_auth_123"
        val data = UserOnboardingData(name = "Jane")

        useCase(data)

        coVerify { repository.saveUserData(data, "user_auth_123", isCompleted = false) }
    }

    @Test
    fun `should fallback to onboardingRepository getUserId if authRepository returns null`() = runBlocking {
        every { authRepository.getCurrentUserId() } returns null
        every { repository.getUserId() } returns flowOf("user_onboarding_123")
        val data = UserOnboardingData(name = "Jane")

        useCase(data)

        coVerify { repository.saveUserData(data, "user_onboarding_123", isCompleted = false) }
    }
}
