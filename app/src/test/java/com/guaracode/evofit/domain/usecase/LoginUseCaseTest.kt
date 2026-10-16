package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginUseCaseTest {

    private val repository: AuthRepository = mockk(relaxed = true)
    private val useCase = LoginUseCaseImpl(repository)

    @Test
    fun `when email or password is blank, should return failure`() = runBlocking {
        val result = useCase("", "123456")

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    @Test
    fun `when email and password are valid, should delegate to repository`() = runBlocking {
        coEvery { repository.login("user@test.com", "123456") } returns Result.success(Unit)

        val result = useCase("user@test.com", "123456")

        assertTrue(result.isSuccess)
        coVerify { repository.login("user@test.com", "123456") }
    }
}
