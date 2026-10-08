package com.guaracode.evofit.presentation.ui.feature.profile.support.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guaracode.evofit.domain.usecase.SendSupportEmailUseCase
import com.guaracode.evofit.presentation.ui.feature.profile.support.state.SupportEmailUiState
import com.guaracode.evofit.presentation.ui.feature.profile.support.tracking.SupportTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SupportEmailViewModel(
    private val sendSupportEmailUseCase: SendSupportEmailUseCase,
    private val tracker: SupportTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(SupportEmailUiState())
    val uiState: StateFlow<SupportEmailUiState> = _uiState.asStateFlow()

    fun sendEmail(topic: String, message: String) {
        tracker.trackSupportEmailSent(topic)
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            sendSupportEmailUseCase(topic, message)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = it.message)
                }
        }
    }

    fun resetState() {
        _uiState.value = SupportEmailUiState()
    }
}
