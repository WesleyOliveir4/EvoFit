package com.example.evofit.presentation.ui.feature.evo.home.state

import com.example.evofit.domain.model.StrengthGain
import com.example.evofit.domain.model.MuscleEvolution

data class EvoHomeUiState(
    val isLoading: Boolean = false,
    val selectedPeriod: String = "1 mês",
    val strengthGains: List<StrengthGain>? = null,
    val mostEvolvedMuscle: MuscleEvolution? = null,
    val workoutsCount: Int = 0
)
