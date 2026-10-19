package com.guaracode.evofit.presentation.ui.feature.workout.startworkout.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class StartWorkoutTrackerImpl(
    private val crashReporter: CrashReporter
) : StartWorkoutTracker {

    override fun trackWorkoutPreviewScreenView(workoutId: String) {
        crashReporter.logEvent("screen_view: workout_preview (id=$workoutId)")
        crashReporter.setCustomKey("current_screen", "workout_preview")
    }

    override fun trackWorkoutStartScreenView(workoutId: String) {
        crashReporter.logEvent("screen_view: workout_start (id=$workoutId)")
        crashReporter.setCustomKey("current_screen", "workout_start")
    }

    override fun trackWorkoutSessionStarted(workoutId: String) {
        crashReporter.logEvent("workout_session_started (id=$workoutId)")
    }

    override fun trackSetCompleted(workoutId: String, exerciseId: String, setNumber: Int) {
        crashReporter.logEvent("workout_set_completed (workout_id=$workoutId, exercise_id=$exerciseId, set_number=$setNumber)")
    }

    override fun trackWorkoutFinished(workoutId: String) {
        crashReporter.logEvent("workout_session_finished (id=$workoutId)")
    }

    override fun trackWorkoutSessionDiscarded(workoutId: String) {
        crashReporter.logEvent("workout_session_discarded (id=$workoutId)")
    }
}
