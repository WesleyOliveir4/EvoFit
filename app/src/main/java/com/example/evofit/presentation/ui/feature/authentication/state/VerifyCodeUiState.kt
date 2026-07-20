package com.example.evofit.presentation.ui.feature.authentication.state

data class VerifyCodeUiState(
    val code: String = "",
    val secondsRemaining: Int = 45,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val oobCode: String? = null,
    val error: String? = null
)
