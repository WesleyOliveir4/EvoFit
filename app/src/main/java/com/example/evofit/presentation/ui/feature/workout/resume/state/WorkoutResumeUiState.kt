package com.example.evofit.presentation.ui.feature.workout.resume.state

data class WorkoutResumeUiState(
    val workoutName: String = "",
    val totalExercises: Int = 0,
    val totalSets: Int = 0,
    val formattedDate: String = "",
    val isLoading: Boolean = false
)
