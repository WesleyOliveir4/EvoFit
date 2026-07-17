package com.example.evofit.presentation.ui.feature.profile.home.state

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileUiState(
    val name: String = "",
    val age: String = "",
    val weight: String = "",
    val totalWorkouts: String = "0",
    val records: String = "0",
    val goals: String = "0",
    val isLoading: Boolean = false
)