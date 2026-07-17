package com.example.evofit.presentation.ui.feature.home.state

import androidx.compose.runtime.Immutable
import com.example.evofit.presentation.model.GoalUIModel

@Immutable
data class HomeUiState(
    val userName: String = "",
    val goals: List<GoalUIModel> = emptyList()
)
