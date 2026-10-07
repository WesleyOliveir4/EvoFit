package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.AuthRepository
import com.example.evofit.domain.repository.OnboardingRepository
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class CompleteOnboardingUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val firebaseAuth: FirebaseAuth = mockk(relaxed = true)

    private val useCase = CompleteOnboardingUseCaseImpl(repository, authRepository, firebaseAuth)

    @Test
    fun `when invoke called, should save user data with isCompleted true and call completeOnboarding`() = runBlocking {
        every { authRepository.getCurrentUserId() } returns "user123"
        every { firebaseAuth.currentUser } returns null

        val data = UserOnboardingData(name = "Alex")

        val result = useCase(data)

        assertTrue(result.isSuccess)
        coVerify { repository.saveUserData(data, "user123", isCompleted = true) }
        coVerify { repository.completeOnboarding() }
    }
}
