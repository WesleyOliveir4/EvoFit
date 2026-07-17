package com.example.evofit.presentation.ui.feature.onboard.state

import com.example.evofit.presentation.model.GoalUIModel

data class OnboardingUiState(
    val name: String = "",
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val goals: List<GoalUIModel> = emptyList()
)
