package com.example.evofit.presentation.ui.feature.workout.home.tracking

import com.example.evofit.core.monitoring.CrashReporter

class WorkoutHomeTrackerImpl(
    private val crashReporter: CrashReporter
) : WorkoutHomeTracker {

    override fun trackWorkoutHomeScreenView() {
        crashReporter.logEvent("screen_view: workout_home")
        crashReporter.setCustomKey("current_screen", "workout_home")
    }

    override fun trackTabChanged(tabName: String) {
        crashReporter.logEvent("workout_home_tab_changed: $tabName")
    }

    override fun trackNewWorkoutClicked() {
        crashReporter.logEvent("ui_click: create_new_workout_button")
    }

    override fun trackWorkoutCardClicked(workoutId: String) {
        crashReporter.logEvent("ui_click: workout_card (id=$workoutId)")
    }

    override fun trackWorkoutDoneClicked(workoutDoneId: String) {
        crashReporter.logEvent("ui_click: workout_done_item (id=$workoutDoneId)")
    }
}
