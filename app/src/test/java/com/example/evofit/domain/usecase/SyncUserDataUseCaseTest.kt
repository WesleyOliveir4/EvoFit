package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.OnboardingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SyncUserDataUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val useCase = SyncUserDataUseCaseImpl(repository)

    @Test
    fun `should delegate syncUserData to repository with appropriate parameters`() = runBlocking {
        coEvery { repository.syncUserData("user123", shouldClearActiveSession = false, isOnline = true) } returns Result.success(Unit)

        val result = useCase("user123", shouldClearActiveSession = false, isOnline = true, forceFullSync = false)

        assertTrue(result.isSuccess)
        coVerify { repository.syncUserData("user123", false, true) }
    }

    @Test
    fun `when forceFullSync is true, should set shouldClearActiveSession parameter in repository call`() = runBlocking {
        coEvery { repository.syncUserData("user123", shouldClearActiveSession = true, isOnline = true) } returns Result.success(Unit)

        val result = useCase("user123", shouldClearActiveSession = false, isOnline = true, forceFullSync = true)

        assertTrue(result.isSuccess)
        coVerify { repository.syncUserData("user123", true, true) }
    }
}
