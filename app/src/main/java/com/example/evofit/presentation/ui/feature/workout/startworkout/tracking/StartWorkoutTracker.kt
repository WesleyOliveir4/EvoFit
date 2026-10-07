package com.example.evofit.presentation.ui.feature.workout.startworkout.tracking

interface StartWorkoutTracker {
    fun trackWorkoutPreviewScreenView(workoutId: String)
    fun trackWorkoutStartScreenView(workoutId: String)
    fun trackWorkoutSessionStarted(workoutId: String)
    fun trackSetCompleted(workoutId: String, exerciseId: String, setNumber: Int)
    fun trackWorkoutFinished(workoutId: String)
    fun trackWorkoutSessionDiscarded(workoutId: String)
}
