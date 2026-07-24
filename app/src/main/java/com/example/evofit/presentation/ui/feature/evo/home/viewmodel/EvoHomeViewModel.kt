package com.example.evofit.presentation.ui.feature.evo.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.EvoPeriod
import com.example.evofit.domain.usecase.GetEvoHomeSummaryUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.presentation.model.MuscleEvolutionUIModel
import com.example.evofit.presentation.model.StrengthGainUIModel
import com.example.evofit.presentation.ui.feature.evo.home.state.EvoHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvoHomeViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getEvoHomeSummaryUseCase: GetEvoHomeSummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EvoHomeUiState())
    val uiState: StateFlow<EvoHomeUiState> = _uiState.asStateFlow()

    init {
        loadData(_uiState.value.selectedPeriod)
    }

    fun onPeriodSelected(period: EvoPeriod) {
        _uiState.update { it.copy(selectedPeriod = period) }
        loadData(period)
    }

    private fun loadData(period: EvoPeriod) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val userId = getUserIdUseCase() ?: return@launch
            
            getEvoHomeSummaryUseCase(userId, period).collect { summary ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        strengthGains = summary.strengthGains?.map {
                            StrengthGainUIModel(exerciseName = it.exerciseName, gainKg = it.gainKg)
                        },
                        mostEvolvedMuscle = summary.mostEvolvedMuscle?.let {
                            MuscleEvolutionUIModel(
                                muscleGroupName = it.muscleGroupName,
                                evolutionPercentage = it.evolutionPercentage
                            )
                        },
                        workoutsCount = summary.workoutsCount,
                        leastTrainedGroup = summary.leastTrainedGroup,
                        kmPerWeek = summary.kmPerWeek,
                        averageWorkoutTime = summary.averageWorkoutTime
                    )
                }
            }
        }
    }
}
