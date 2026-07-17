package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.repository.ExerciseRepository

interface GetGoalSuggestionsUseCase {
    operator fun invoke(): List<GoalSuggestion>
}

class GetGoalSuggestionsUseCaseImpl(
    private val repository: ExerciseRepository
) : GetGoalSuggestionsUseCase {
    override fun invoke(): List<GoalSuggestion> = repository.getSuggestions()
}
