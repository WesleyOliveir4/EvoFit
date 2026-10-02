package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SendPasswordResetCodeUseCaseTest {

    private val repository: AuthRepository = mockk(relaxed = true)
    private val useCase = SendPasswordResetCodeUseCaseImpl(repository)

    @Test
    fun `when email is blank, should return failure`() = runBlocking {
        val result = useCase("")

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.sendPasswordResetCode(any()) }
    }

    @Test
    fun `when email format is invalid, should return failure`() = runBlocking {
        val result = useCase("invalid-email")

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.sendPasswordResetCode(any()) }
    }

    @Test
    fun `when email is valid, should delegate to repository`() = runBlocking {
        coEvery { repository.sendPasswordResetCode("user@test.com") } returns Result.success(Unit)

        val result = useCase("user@test.com")

        assertTrue(result.isSuccess)
        coVerify { repository.sendPasswordResetCode("user@test.com") }
    }
}
