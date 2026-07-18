package com.example.evofit.presentation.ui.feature.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.presentation.ui.feature.authentication.state.VerifyCodeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyCodeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyCodeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_uiState.value.secondsRemaining > 0) {
                delay(1000)
                _uiState.update { it.copy(secondsRemaining = it.secondsRemaining - 1) }
            }
        }
    }

    fun onCodeChange(code: String) {
        _uiState.update { it.copy(code = code, error = null) }
    }

    fun onResendClick() {
        _uiState.update { it.copy(secondsRemaining = 45, error = null) }
        startTimer()
        // Chamada ao UseCase no Passo 3
    }

    fun onVerifyClick() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        // Chamada ao UseCase no Passo 3
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
