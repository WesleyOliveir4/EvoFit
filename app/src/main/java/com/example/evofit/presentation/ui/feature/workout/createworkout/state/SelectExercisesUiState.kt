package com.example.evofit.presentation.ui.feature.workout.createworkout.state

import androidx.compose.runtime.Immutable
import com.example.evofit.presentation.model.ExerciseSelectionUIModel

@Immutable
data class SelectExercisesUiState(
    val muscleGroupName: String = "",
    val workoutName: String = "",
    val tempWorkoutName: String = "",
    val isEditingName: Boolean = false,
    val exercises: List<ExerciseSelectionUIModel> = emptyList(),
    val isLoading: Boolean = false,
    val editWorkoutId: String? = null,
    val showCancelEditDialog: Boolean = false
)
