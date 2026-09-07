package com.example.evofit.presentation.ui.feature.evo.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.EvoPeriod
import com.example.evofit.domain.usecase.GetEvoHomeSummaryUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.presentation.model.MuscleEvolutionUIModel
import com.example.evofit.presentation.model.StrengthGainUIModel
import com.example.evofit.presentation.ui.feature.evo.home.state.EvoHomeUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class EvoHomeViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getEvoHomeSummaryUseCase: GetEvoHomeSummaryUseCase
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(EvoPeriod.LAST_30_DAYS)

    val uiState: StateFlow<EvoHomeUiState> = combine(
        _selectedPeriod,
        getUserIdUseCase()
    ) { period, userId ->
        period to userId
    }.flatMapLatest { (period, userId) ->
        if (userId.isNullOrEmpty()) {
            flowOf(EvoHomeUiState(selectedPeriod = period, isLoading = false))
        } else {
            getEvoHomeSummaryUseCase(userId, period).map { summary ->
                EvoHomeUiState(
                    isLoading = false,
                    selectedPeriod = period,
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
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EvoHomeUiState(isLoading = true)
    )

    fun onPeriodSelected(period: EvoPeriod) {
        _selectedPeriod.value = period
    }
}
