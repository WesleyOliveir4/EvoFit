package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.CompletedSet
import com.guaracode.evofit.domain.repository.WorkoutSessionRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UpdateCompletedSetsUseCaseTest {

    private val repository: WorkoutSessionRepository = mockk(relaxed = true)
    private val useCase = UpdateCompletedSetsUseCaseImpl(repository)

    @Test
    fun `should delegate updateCompletedSets to repository`() = runBlocking {
        val sets = listOf(CompletedSet("we1", 1))

        useCase(sets)

        coVerify { repository.updateCompletedSets(sets) }
    }
}
