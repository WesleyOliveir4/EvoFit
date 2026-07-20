package com.example.evofit.presentation.ui.feature.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.SendPasswordResetCodeUseCase
import com.example.evofit.domain.usecase.VerifyPasswordResetCodeUseCase
import com.example.evofit.presentation.ui.feature.authentication.state.VerifyCodeUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyCodeViewModel(
    private val sendPasswordResetCodeUseCase: SendPasswordResetCodeUseCase,
    private val verifyPasswordResetCodeUseCase: VerifyPasswordResetCodeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyCodeUiState())
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(secondsRemaining = 45) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.secondsRemaining > 0) {
                delay(1000)
                _uiState.update { it.copy(secondsRemaining = it.secondsRemaining - 1) }
            }
        }
    }

    fun onCodeChange(code: String) {
        _uiState.update { it.copy(code = code, error = null) }
    }

    fun onResendClick(email: String) {
        if (_uiState.value.secondsRemaining > 0) return
        
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            sendPasswordResetCodeUseCase(email)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    startTimer()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun onVerifyClick(email: String) {
        val code = _uiState.value.code
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            verifyPasswordResetCodeUseCase(email, code)
                .onSuccess { oobCode ->
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, oobCode = oobCode) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
