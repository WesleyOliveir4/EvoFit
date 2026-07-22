package com.example.evofit.presentation.ui.feature.workout.resume.state

import androidx.compose.runtime.Immutable

enum class ResumeMode {
    CREATED,
    UPDATED,
    COMPLETED,
    CANCELLED
}

@Immutable
data class WorkoutResumeUiState(
    val workoutName: String = "",
    val totalExercises: Int = 0,
    val totalSets: Int = 0,
    val completedSets: Int? = null,
    val duration: String? = null,
    val formattedDate: String = "",
    val isLoading: Boolean = false,
    val mode: ResumeMode = ResumeMode.CREATED
)
