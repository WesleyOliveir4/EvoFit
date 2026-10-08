package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.OnboardingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUserIdUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val useCase = GetUserIdUseCaseImpl(repository)

    @Test
    fun `should delegate invoke to onboardingRepository getUserId`() = runBlocking {
        every { repository.getUserId() } returns flowOf("u123")

        val result = useCase().first()

        assertEquals("u123", result)
        verify { repository.getUserId() }
    }
}
