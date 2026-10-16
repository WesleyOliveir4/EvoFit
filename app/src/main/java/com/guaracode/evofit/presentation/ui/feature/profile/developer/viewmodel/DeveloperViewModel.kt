package com.guaracode.evofit.presentation.ui.feature.profile.developer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guaracode.evofit.domain.usecase.GenerateFakeWorkoutHistoryUseCase
import com.guaracode.evofit.presentation.ui.feature.profile.developer.state.DeveloperUiState
import com.guaracode.evofit.presentation.ui.feature.profile.developer.tracking.DeveloperTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeveloperViewModel(
    private val generateFakeWorkoutHistoryUseCase: GenerateFakeWorkoutHistoryUseCase,
    private val tracker: DeveloperTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeveloperUiState())
    val uiState = _uiState.asStateFlow()

    fun generateFakeHistory() {
        tracker.trackGenerateFakeHistoryClicked()
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingHistory = true, generationSuccess = false) }
            try {
                generateFakeWorkoutHistoryUseCase()
                _uiState.update { it.copy(generationSuccess = true) }
            } finally {
                _uiState.update { it.copy(isGeneratingHistory = false) }
            }
        }
    }
    
    fun resetSuccessState() {
        _uiState.update { it.copy(generationSuccess = false) }
    }
}
