package com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetTrainedMuscleGroupsUseCase
import com.example.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvoAnalyticsViewModel(
    private val getTrainedMuscleGroupsUseCase: GetTrainedMuscleGroupsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EvoAnalyticsState())
    val uiState: StateFlow<EvoAnalyticsState> = _uiState.asStateFlow()

    init {
        loadTrainedGroups()
    }

    private fun loadTrainedGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val groups = getTrainedMuscleGroupsUseCase()
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        trainedGroups = groups
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    ) 
                }
            }
        }
    }
}
