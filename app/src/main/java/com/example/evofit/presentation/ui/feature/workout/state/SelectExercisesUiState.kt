package com.example.evofit.presentation.ui.feature.workout.state

import com.example.evofit.presentation.model.ExerciseSelectionUIModel

data class SelectExercisesUiState(
    val muscleGroupName: String = "",
    val workoutName: String = "",
    val tempWorkoutName: String = "",
    val isEditingName: Boolean = false,
    val exercises: List<ExerciseSelectionUIModel> = emptyList(),
    val isLoading: Boolean = false
)