package com.guaracode.evofit.presentation.ui.feature.workout.resume.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class WorkoutResumeTrackerImpl(
    private val crashReporter: CrashReporter
) : WorkoutResumeTracker {

    override fun trackWorkoutResumeScreenView(workoutName: String) {
        crashReporter.logEvent("screen_view: workout_resume (name=$workoutName)")
        crashReporter.setCustomKey("current_screen", "workout_resume")
    }

    override fun trackWorkoutDoneShared() {
        crashReporter.logEvent("workout_resume_shared")
    }
}
