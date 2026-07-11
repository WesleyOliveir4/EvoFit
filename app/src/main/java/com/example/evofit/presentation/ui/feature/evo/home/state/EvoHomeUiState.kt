package com.example.evofit.presentation.ui.feature.evo.home.state

import com.example.evofit.domain.model.EvoPeriod
import com.example.evofit.domain.model.StrengthGain
import com.example.evofit.domain.model.MuscleEvolution

data class EvoHomeUiState(
    val isLoading: Boolean = false,
    val selectedPeriod: EvoPeriod = EvoPeriod.LAST_30_DAYS,
    val strengthGains: List<StrengthGain>? = null,
    val mostEvolvedMuscle: MuscleEvolution? = null,
    val workoutsCount: Int = 0,
    val leastTrainedGroup: Pair<String, Int>? = null,
    val kmPerWeek: Double = 0.0,
    val averageWorkoutTime: Int = 0
)
