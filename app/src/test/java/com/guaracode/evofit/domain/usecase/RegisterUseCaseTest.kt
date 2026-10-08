package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterUseCaseTest {

    private val repository: AuthRepository = mockk(relaxed = true)
    private val useCase = RegisterUseCaseImpl(repository)

    @Test
    fun `when email or password is blank, should return failure`() = runBlocking {
        val result1 = useCase("", "123456")
        val result2 = useCase("email@test.com", "")

        assertTrue(result1.isFailure)
        assertTrue(result2.isFailure)
        coVerify(exactly = 0) { repository.register(any(), any()) }
    }

    @Test
    fun `when password length is less than 6, should return failure`() = runBlocking {
        val result = useCase("email@test.com", "12345")

        assertTrue(result.isFailure)
        assertEquals("Password must be at least 6 characters", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.register(any(), any()) }
    }

    @Test
    fun `when email and password are valid, should call repository register`() = runBlocking {
        coEvery { repository.register("email@test.com", "123456") } returns Result.success(Unit)

        val result = useCase("email@test.com", "123456")

        assertTrue(result.isSuccess)
        coVerify { repository.register("email@test.com", "123456") }
    }
}
