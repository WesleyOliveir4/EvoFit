package com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.usecase.NukeUserDataUseCase
import com.guaracode.evofit.domain.usecase.RegisterUseCase
import com.guaracode.evofit.domain.usecase.SyncUserDataUseCase
import com.guaracode.evofit.presentation.mapper.AuthErrorMapper
import com.guaracode.evofit.presentation.ui.feature.authentication.state.RegisterUiState
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val authRepository: AuthRepository,
    private val syncUserDataUseCase: SyncUserDataUseCase,
    private val nukeUserDataUseCase: NukeUserDataUseCase,
    private val errorMapper: AuthErrorMapper,
    private val authTracker: AuthTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.update { it.copy(confirmPassword = password, error = null) }
    }

    fun onTermsAcceptedChange(accepted: Boolean) {
        _uiState.update { it.copy(termsAccepted = accepted) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun onRegisterClick() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        authTracker.trackRegisterClicked()
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            registerUseCase(
                email = currentState.email,
                password = currentState.password
            ).onSuccess {
                authTracker.trackRegisterSuccess()
                val userId = authRepository.getCurrentUserId()
                if (userId != null) {
                    nukeUserDataUseCase()
                    syncUserDataUseCase(userId, shouldClearActiveSession = true, isOnline = true)
                }
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                authTracker.trackRegisterFailure(error.message)
                _uiState.update { it.copy(isLoading = false, error = errorMapper.map(error)) }
            }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
