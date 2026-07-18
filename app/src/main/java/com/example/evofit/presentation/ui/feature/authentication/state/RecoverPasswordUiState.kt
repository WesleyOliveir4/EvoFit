package com.example.evofit.presentation.ui.feature.authentication.state

data class RecoverPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
