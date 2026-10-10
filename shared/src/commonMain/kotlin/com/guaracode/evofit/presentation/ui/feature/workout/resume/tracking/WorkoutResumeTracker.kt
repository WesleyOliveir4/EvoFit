package com.guaracode.evofit.presentation.ui.feature.workout.resume.tracking

interface WorkoutResumeTracker {
    fun trackWorkoutResumeScreenView(workoutName: String)
    fun trackWorkoutDoneShared()
}
