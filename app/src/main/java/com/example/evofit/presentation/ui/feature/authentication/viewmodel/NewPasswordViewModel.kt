package com.example.evofit.presentation.ui.feature.authentication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.evofit.presentation.ui.feature.authentication.state.NewPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewPasswordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.update { it.copy(confirmPassword = password, error = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun onSaveClick() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        // Chamada ao UseCase no Passo 3
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
