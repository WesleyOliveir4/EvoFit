package com.example.evofit.presentation.ui.feature.workout.home.tracking

interface WorkoutHomeTracker {
    fun trackWorkoutHomeScreenView()
    fun trackTabChanged(tabName: String)
    fun trackNewWorkoutClicked()
    fun trackWorkoutCardClicked(workoutId: String)
    fun trackWorkoutDoneClicked(workoutDoneId: String)
}
