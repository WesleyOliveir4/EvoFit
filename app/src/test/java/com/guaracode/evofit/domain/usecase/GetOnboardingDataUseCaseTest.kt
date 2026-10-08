package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.repository.OnboardingRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetOnboardingDataUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val useCase = GetOnboardingDataUseCaseImpl(repository)

    @Test
    fun `when repository returns user onboarding data, should emit data`() = runBlocking {
        val data = UserOnboardingData(name = "John", weight = "80", height = "180")
        every { repository.getUserData() } returns flowOf(data)

        val result = useCase().first()

        assertEquals("John", result.name)
        assertEquals("80", result.weight)
    }

    @Test
    fun `when repository returns null, should emit empty UserOnboardingData`() = runBlocking {
        every { repository.getUserData() } returns flowOf(null)

        val result = useCase().first()

        assertEquals("", result.name)
        assertEquals("", result.weight)
    }
}
