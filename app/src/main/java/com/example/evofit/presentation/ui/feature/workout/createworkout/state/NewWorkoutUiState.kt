package com.example.evofit.presentation.ui.feature.workout.createworkout.state

import com.example.evofit.domain.model.MuscleGroup

data class NewWorkoutUiState(
    val muscleGroups: List<MuscleGroup> = emptyList(),
    val isLoading: Boolean = false
)
