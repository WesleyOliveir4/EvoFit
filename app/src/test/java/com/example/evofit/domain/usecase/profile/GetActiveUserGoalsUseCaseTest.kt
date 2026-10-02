package com.example.evofit.domain.usecase.profile

import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetActiveUserGoalsUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val useCase = GetActiveUserGoalsUseCaseImpl(repository)

    @Test
    fun `when userData has goals, should return active user goals`() = runBlocking {
        val goal = UserGoal.Weight(id = "g1", targetWeight = "70")
        val userData = UserOnboardingData(goals = listOf(goal))
        every { repository.getUserData() } returns flowOf(userData)

        val result = useCase().first()

        assertEquals(1, result.size)
        assertEquals("g1", result.first().id)
        verify { repository.getUserData() }
    }

    @Test
    fun `when userData is null, should return empty list`() = runBlocking {
        every { repository.getUserData() } returns flowOf(null)

        val result = useCase().first()

        assertEquals(0, result.size)
    }
}
