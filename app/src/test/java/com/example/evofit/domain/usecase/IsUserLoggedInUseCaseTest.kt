package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Test

class IsUserLoggedInUseCaseTest {

    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val useCase = IsUserLoggedInUseCaseImpl(authRepository)

    @Test
    fun `should delegate invoke to authRepository isLoggedIn`() {
        every { authRepository.isLoggedIn() } returns true

        val result = useCase()

        assertTrue(result)
        verify { authRepository.isLoggedIn() }
    }
}
