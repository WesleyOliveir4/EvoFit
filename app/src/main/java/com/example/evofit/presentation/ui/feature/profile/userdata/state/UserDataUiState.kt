package com.example.evofit.presentation.ui.feature.profile.userdata.state

data class UserDataUiState(
    val name: String = "",
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)