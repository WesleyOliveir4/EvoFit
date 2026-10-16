package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class LogoutUseCaseTest {

    private val repository: AuthRepository = mockk(relaxed = true)
    private val useCase = LogoutUseCase(repository)

    @Test
    fun `should delegate logout to repository`() = runBlocking {
        coEvery { repository.logout() } returns Result.success(Unit)

        val result = useCase()

        assertTrue(result.isSuccess)
        coVerify { repository.logout() }
    }
}
