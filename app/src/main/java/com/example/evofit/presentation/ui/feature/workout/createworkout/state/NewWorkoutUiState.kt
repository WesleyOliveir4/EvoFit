package com.example.evofit.presentation.ui.feature.workout.createworkout.state

import com.example.evofit.data.model.MuscleGroupModel

data class NewWorkoutUiState(
    val muscleGroups: List<MuscleGroupModel> = emptyList(),
    val isLoading: Boolean = false
)