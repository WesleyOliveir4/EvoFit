package com.example.evofit.presentation.ui.feature.workout.createworkout.state

import androidx.compose.runtime.Immutable
import com.example.evofit.presentation.model.MuscleGroupItem

@Immutable
data class NewWorkoutUiState(
    val muscleGroups: List<MuscleGroupItem> = emptyList(),
    val selectedMuscleGroupIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val editWorkoutId: String? = null,
    val showCancelEditDialog: Boolean = false
)
