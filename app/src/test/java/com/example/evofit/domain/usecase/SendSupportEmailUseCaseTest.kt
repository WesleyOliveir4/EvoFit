package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.SupportRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SendSupportEmailUseCaseTest {

    private val repository: SupportRepository = mockk(relaxed = true)
    private val useCase = SendSupportEmailUseCaseImpl(repository)

    @Test
    fun `should prefix topic with EvoFit and delegate to supportRepository`() = runBlocking {
        coEvery { repository.sendSupportEmail("EvoFit - Bug", "App crash on start") } returns Result.success(Unit)

        val result = useCase("Bug", "App crash on start")

        assertTrue(result.isSuccess)
        coVerify { repository.sendSupportEmail("EvoFit - Bug", "App crash on start") }
    }
}
