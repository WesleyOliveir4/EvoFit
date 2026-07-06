package com.example.evofit.presentation.ui.feature.workout.resume.state

data class WorkoutResumeUiState(
    val workoutName: String = "",
    val totalExercises: Int = 0,
    val totalSets: Int = 0,
    val completedSets: Int? = null,
    val duration: String? = null,
    val formattedDate: String = "",
    val isLoading: Boolean = false,
    val isWorkoutDone: Boolean = false
)
