package com.guaracode.evofit.presentation.ui.feature.evo.home.tracking

interface EvoHomeTracker {
    fun trackEvoHomeScreenView()
    fun trackPeriodSelected(periodName: String)
    fun trackExerciseAnalyticsCardClicked()
}
