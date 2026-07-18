package com.example.evofit.presentation.ui.feature.workout.startworkout.state

import androidx.compose.runtime.Immutable
import com.example.evofit.presentation.model.WorkoutDetailPreview

@Immutable
data class WorkoutPreviewUiState(
    val preview: WorkoutDetailPreview? = null,
    val hasActiveSessionConflict: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isDeleted: Boolean = false,
    val showEditBlockedDialog: Boolean = false,
    val showDeleteBlockedDialog: Boolean = false
)
