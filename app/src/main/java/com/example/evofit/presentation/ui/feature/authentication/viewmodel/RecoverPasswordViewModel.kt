package com.example.evofit.presentation.ui.feature.authentication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.evofit.presentation.ui.feature.authentication.state.RecoverPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecoverPasswordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RecoverPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onSendCodeClick() {
        // A chamada ao UseCase será implementada no Passo 3
        _uiState.update { it.copy(isLoading = true, error = null) }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
