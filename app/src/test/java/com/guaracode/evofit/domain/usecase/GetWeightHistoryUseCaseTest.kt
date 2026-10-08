package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WeightUpdate
import com.guaracode.evofit.domain.repository.OnboardingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWeightHistoryUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val useCase = GetWeightHistoryUseCaseImpl(repository)

    @Test
    fun `should delegate invoke to onboardingRepository getWeightHistory`() = runBlocking {
        val history = listOf(WeightUpdate("w1", "80.0", "01/01/2026"))
        every { repository.getWeightHistory("user123") } returns flowOf(history)

        val result = useCase("user123").first()

        assertEquals(1, result.size)
        assertEquals("80.0", result.first().weight)
        verify { repository.getWeightHistory("user123") }
    }
}
