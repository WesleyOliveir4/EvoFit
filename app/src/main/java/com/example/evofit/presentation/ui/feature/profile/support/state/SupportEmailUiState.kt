package com.example.evofit.presentation.ui.feature.profile.support.state

data class SupportEmailUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
