package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.ExerciseCategory
import com.guaracode.evofit.domain.model.GoalSuggestion
import com.guaracode.evofit.domain.repository.ExerciseRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class GetGoalSuggestionsUseCaseTest {

    private val repository: ExerciseRepository = mockk(relaxed = true)
    private val useCase = GetGoalSuggestionsUseCaseImpl(repository)

    @Test
    fun `should delegate to exerciseRepository getSuggestions`() {
        val suggestions = listOf(GoalSuggestion("1", "Ganhar Peso", isWeightGoal = true))
        every { repository.getSuggestions() } returns suggestions

        val result = useCase()

        assertEquals(1, result.size)
        assertEquals("Ganhar Peso", result.first().text)
        verify { repository.getSuggestions() }
    }
}
