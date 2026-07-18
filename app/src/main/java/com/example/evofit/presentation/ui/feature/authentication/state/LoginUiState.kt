package com.example.evofit.presentation.ui.feature.authentication.state

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isOnboardingCompleted: Boolean = true,
    val error: String? = null
)
