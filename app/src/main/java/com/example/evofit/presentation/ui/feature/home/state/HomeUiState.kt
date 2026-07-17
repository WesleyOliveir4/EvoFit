package com.example.evofit.presentation.ui.feature.home.state

import com.example.evofit.presentation.model.GoalUIModel

data class HomeUiState(
    val userName: String = "",
    val goals: List<GoalUIModel> = emptyList()
)
