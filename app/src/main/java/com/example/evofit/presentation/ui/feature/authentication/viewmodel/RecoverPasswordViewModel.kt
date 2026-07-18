package com.example.evofit.presentation.ui.feature.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.SendPasswordResetCodeUseCase
import com.example.evofit.presentation.ui.feature.authentication.state.RecoverPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecoverPasswordViewModel(
    private val sendPasswordResetCodeUseCase: SendPasswordResetCodeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecoverPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onSendCodeClick() {
        val email = _uiState.value.email
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            sendPasswordResetCodeUseCase(email)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
