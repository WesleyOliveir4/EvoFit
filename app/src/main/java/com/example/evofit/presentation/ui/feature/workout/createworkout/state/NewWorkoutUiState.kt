package com.example.evofit.presentation.ui.feature.workout.createworkout.state

import com.example.evofit.presentation.model.MuscleGroupItem

data class NewWorkoutUiState(
    val muscleGroups: List<MuscleGroupItem> = emptyList(),
    val isLoading: Boolean = false
)
