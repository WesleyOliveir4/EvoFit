package com.example.evofit.presentation.ui.feature.evo.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetMostEvolvedMuscleUseCase
import com.example.evofit.domain.usecase.GetStrengthGainsUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutsCountUseCase
import com.example.evofit.presentation.ui.feature.evo.home.state.EvoHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvoHomeViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getStrengthGainsUseCase: GetStrengthGainsUseCase,
    private val getMostEvolvedMuscleUseCase: GetMostEvolvedMuscleUseCase,
    private val getWorkoutsCountUseCase: GetWorkoutsCountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EvoHomeUiState())
    val uiState: StateFlow<EvoHomeUiState> = _uiState.asStateFlow()

    init {
        loadData(_uiState.value.selectedPeriod)
    }

    fun onPeriodSelected(period: String) {
        _uiState.update { it.copy(selectedPeriod = period) }
        loadData(period)
    }

    private fun loadData(period: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val userId = getUserIdUseCase() ?: return@launch
            
            val gains = getStrengthGainsUseCase(userId, period)
            val evolution = getMostEvolvedMuscleUseCase(userId, period)
            val count = getWorkoutsCountUseCase(userId, period)

            _uiState.update { 
                it.copy(
                    isLoading = false,
                    strengthGains = gains,
                    mostEvolvedMuscle = evolution,
                    workoutsCount = count
                )
            }
        }
    }
}
