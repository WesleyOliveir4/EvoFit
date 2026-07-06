package com.example.evofit.presentation.ui.feature.workout.startworkout.state

import com.example.evofit.presentation.model.WorkoutDetailPreview

data class WorkoutPreviewUiState(
    val preview: WorkoutDetailPreview? = null,
    val hasActiveSessionConflict: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isDeleted: Boolean = false,
    val showEditBlockedDialog: Boolean = false,
    val showDeleteBlockedDialog: Boolean = false
)
