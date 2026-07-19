package com.example.evofit.presentation.ui.feature.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.IsOnboardingCompletedUseCase
import com.example.evofit.domain.usecase.LoginUseCase
import com.example.evofit.domain.usecase.LoginWithGoogleUseCase
import com.example.evofit.domain.usecase.LoginWithAppleUseCase
import com.example.evofit.presentation.ui.feature.authentication.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithAppleUseCase: LoginWithAppleUseCase,
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        determineSocialLoginAvailability()
    }

    private fun determineSocialLoginAvailability() {
        // Regra: Android exibe apenas Google. Outros (ex: KMP) exibem ambos.
        val isAndroid = true 
        _uiState.update { it.copy(showAppleLogin = !isAndroid) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            loginUseCase(currentState.email, currentState.password)
                .onSuccess { handleLoginSuccess() }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun onGoogleLoginClick(idToken: String) {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            loginWithGoogleUseCase(idToken)
                .onSuccess { handleLoginSuccess() }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun onAppleLoginClick() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            loginWithAppleUseCase()
                .onSuccess { handleLoginSuccess() }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private suspend fun handleLoginSuccess() {
        val onboardingCompleted = isOnboardingCompletedUseCase().first()
        _uiState.update { 
            it.copy(
                isLoading = false, 
                isSuccess = true,
                isOnboardingCompleted = onboardingCompleted
            )
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
