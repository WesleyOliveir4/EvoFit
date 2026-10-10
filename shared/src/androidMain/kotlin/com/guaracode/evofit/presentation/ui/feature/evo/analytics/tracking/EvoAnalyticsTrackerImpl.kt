package com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class EvoAnalyticsTrackerImpl(
    private val crashReporter: CrashReporter
) : EvoAnalyticsTracker {
    override fun trackCategoryAnalyticsSelectionScreenView() {
        crashReporter.logEvent("screen_view: evo_analytics_category_selection")
        crashReporter.setCustomKey("current_screen", "evo_analytics_category_selection")
    }

    override fun trackExerciseSelectionScreenView(muscleGroupName: String) {
        crashReporter.logEvent("screen_view: evo_analytics_exercise_selection (group=$muscleGroupName)")
        crashReporter.setCustomKey("current_screen", "evo_analytics_exercise_selection")
    }

    override fun trackCategoryDetailAnalyticsScreenView(exerciseName: String) {
        crashReporter.logEvent("screen_view: evo_analytics_detail (exercise=$exerciseName)")
        crashReporter.setCustomKey("current_screen", "evo_analytics_detail")
    }

    override fun trackMuscleGroupSelected(groupId: String, groupName: String) {
        crashReporter.logEvent("evo_analytics_muscle_group_selected: $groupName")
    }

    override fun trackWeightCategorySelected() {
        crashReporter.logEvent("evo_analytics_weight_category_selected")
    }

    override fun trackExerciseSelected(exerciseId: String, exerciseName: String) {
        crashReporter.logEvent("evo_analytics_exercise_selected: $exerciseName")
    }
}
