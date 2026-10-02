package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginWithAppleUseCaseTest {

    private val repository: AuthRepository = mockk(relaxed = true)
    private val useCase = LoginWithAppleUseCaseImpl(repository)

    @Test
    fun `should delegate invoke to repository loginWithApple`() = runBlocking {
        coEvery { repository.loginWithApple() } returns Result.success(Unit)

        val result = useCase()

        assertTrue(result.isSuccess)
        coVerify { repository.loginWithApple() }
    }
}
