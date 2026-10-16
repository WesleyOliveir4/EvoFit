package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.OnboardingRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NukeUserDataUseCaseTest {

    private val repository: OnboardingRepository = mockk(relaxed = true)
    private val useCase = NukeUserDataUseCaseImpl(repository)

    @Test
    fun `should delegate nukeUserData to repository`() = runBlocking {
        useCase()

        coVerify { repository.nukeUserData() }
    }
}
