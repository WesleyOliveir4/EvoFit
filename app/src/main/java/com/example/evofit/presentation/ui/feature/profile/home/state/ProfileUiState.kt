package com.example.evofit.presentation.ui.feature.profile.home.state

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileUiState(
    val name: String = "",
    val birthDate: String = "",
    val weight: String = "",
    val height: String = "",
    val profilePictureUri: String? = null,
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false
)