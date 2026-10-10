package com.guaracode.evofit.presentation.ui.feature.authentication.state

import androidx.compose.runtime.Immutable

@Immutable
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isOnboardingCompleted: Boolean = true,
    val showAppleLogin: Boolean = false,
    val error: String? = null
)
