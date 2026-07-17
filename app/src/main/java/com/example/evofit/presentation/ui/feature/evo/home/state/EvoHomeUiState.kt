package com.example.evofit.presentation.ui.feature.evo.home.state

import androidx.compose.runtime.Immutable
import com.example.evofit.domain.model.EvoPeriod
import com.example.evofit.presentation.model.MuscleEvolutionUIModel
import com.example.evofit.presentation.model.StrengthGainUIModel

@Immutable
data class EvoHomeUiState(
    val isLoading: Boolean = false,
    val selectedPeriod: EvoPeriod = EvoPeriod.LAST_30_DAYS,
    val strengthGains: List<StrengthGainUIModel>? = null,
    val mostEvolvedMuscle: MuscleEvolutionUIModel? = null,
    val workoutsCount: Int = 0,
    val leastTrainedGroup: Pair<String, Int>? = null,
    val kmPerWeek: Double = 0.0,
    val averageWorkoutTime: Int = 0
)
