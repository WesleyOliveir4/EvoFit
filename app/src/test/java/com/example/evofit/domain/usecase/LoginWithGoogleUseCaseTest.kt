package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginWithGoogleUseCaseTest {

    private val repository: AuthRepository = mockk(relaxed = true)
    private val useCase = LoginWithGoogleUseCaseImpl(repository)

    @Test
    fun `when idToken is blank, should return failure`() = runBlocking {
        val result = useCase("")

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.loginWithGoogle(any()) }
    }

    @Test
    fun `when idToken is valid, should delegate to repository`() = runBlocking {
        coEvery { repository.loginWithGoogle("token123") } returns Result.success(Unit)

        val result = useCase("token123")

        assertTrue(result.isSuccess)
        coVerify { repository.loginWithGoogle("token123") }
    }
}
