package com.example.evofit.presentation.ui.feature.profile.state

data class ProfileUiState(
    val name: String = "",
    val age: String = "",
    val weight: String = "",
    val isLoading: Boolean = false
)