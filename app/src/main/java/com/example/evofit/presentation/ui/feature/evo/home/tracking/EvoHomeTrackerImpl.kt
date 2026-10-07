package com.example.evofit.presentation.ui.feature.evo.home.tracking

import com.example.evofit.core.monitoring.CrashReporter

class EvoHomeTrackerImpl(
    private val crashReporter: CrashReporter
) : EvoHomeTracker {

    override fun trackEvoHomeScreenView() {
        crashReporter.logEvent("screen_view: evo_home")
        crashReporter.setCustomKey("current_screen", "evo_home")
    }

    override fun trackPeriodSelected(periodName: String) {
        crashReporter.logEvent("evo_home_period_selected: $periodName")
    }

    override fun trackExerciseAnalyticsCardClicked() {
        crashReporter.logEvent("ui_click: evo_home_exercise_analytics_card")
    }
}
