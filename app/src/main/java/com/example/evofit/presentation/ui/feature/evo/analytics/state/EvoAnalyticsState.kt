package com.example.evofit.presentation.ui.feature.evo.analytics.state

import com.example.evofit.domain.model.MuscleGroup

data class EvoAnalyticsState(
    val isLoading: Boolean = false,
    val trainedGroups: List<MuscleGroup> = emptyList(),
    val error: String? = null
)
