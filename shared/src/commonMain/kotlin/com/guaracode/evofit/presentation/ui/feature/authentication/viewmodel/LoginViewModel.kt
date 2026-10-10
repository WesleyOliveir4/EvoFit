package com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.usecase.IsOnboardingCompletedUseCase
import com.guaracode.evofit.domain.usecase.LoginUseCase
import com.guaracode.evofit.domain.usecase.LoginWithGoogleUseCase
import com.guaracode.evofit.domain.usecase.LoginWithAppleUseCase
import com.guaracode.evofit.domain.usecase.NukeUserDataUseCase
import com.guaracode.evofit.domain.usecase.SyncUserDataUseCase
import com.guaracode.evofit.presentation.mapper.AuthErrorMapper
import com.guaracode.evofit.presentation.ui.feature.authentication.state.LoginUiState
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithAppleUseCase: LoginWithAppleUseCase,
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
    private val authRepository: AuthRepository,
    private val syncUserDataUseCase: SyncUserDataUseCase,
    private val nukeUserDataUseCase: NukeUserDataUseCase,
    private val errorMapper: AuthErrorMapper,
    private val authTracker: AuthTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        determineSocialLoginAvailability()
    }

    private fun determineSocialLoginAvailability() {
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

        authTracker.trackLoginClicked("email")
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            loginUseCase(currentState.email, currentState.password)
                .onSuccess {
                    authTracker.trackLoginSuccess("email")
                    handleLoginSuccess()
                }
                .onFailure { error ->
                    authTracker.trackLoginFailure("email", error.message)
                    _uiState.update { it.copy(isLoading = false, error = errorMapper.map(error)) }
                }
        }
    }

    fun onGoogleLoginClick(idToken: String) {
        if (_uiState.value.isLoading) return

        authTracker.trackLoginClicked("google")
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            loginWithGoogleUseCase(idToken)
                .onSuccess {
                    authTracker.trackLoginSuccess("google")
                    handleLoginSuccess()
                }
                .onFailure { error ->
                    authTracker.trackLoginFailure("google", error.message)
                    _uiState.update { it.copy(isLoading = false, error = errorMapper.map(error)) }
                }
        }
    }

    fun onAppleLoginClick() {
        if (_uiState.value.isLoading) return

        authTracker.trackLoginClicked("apple")
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            loginWithAppleUseCase()
                .onSuccess {
                    authTracker.trackLoginSuccess("apple")
                    handleLoginSuccess()
                }
                .onFailure { error ->
                    authTracker.trackLoginFailure("apple", error.message)
                    _uiState.update { it.copy(isLoading = false, error = errorMapper.map(error)) }
                }
        }
    }

    private suspend fun handleLoginSuccess() {
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            nukeUserDataUseCase()
            syncUserDataUseCase(userId, shouldClearActiveSession = true, isOnline = true)
        }
        val onboardingCompleted = isOnboardingCompletedUseCase.executeDirect()
        
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
