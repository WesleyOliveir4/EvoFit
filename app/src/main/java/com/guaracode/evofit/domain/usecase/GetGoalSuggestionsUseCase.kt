package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.GoalSuggestion
import com.guaracode.evofit.domain.repository.ExerciseRepository

interface GetGoalSuggestionsUseCase {
    operator fun invoke(): List<GoalSuggestion>
}

class GetGoalSuggestionsUseCaseImpl(
    private val repository: ExerciseRepository
) : GetGoalSuggestionsUseCase {
    override fun invoke(): List<GoalSuggestion> = repository.getSuggestions()
}
