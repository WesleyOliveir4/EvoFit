package com.example.evofit.presentation.ui.feature.onboard.state

import androidx.compose.runtime.Immutable
import com.example.evofit.presentation.model.GoalUIModel

@Immutable
data class OnboardingUiState(
    val name: String = "",
    val birthDate: String = "",
    val weight: String = "",
    val height: String = "",
    val goals: List<GoalUIModel> = emptyList()
)
