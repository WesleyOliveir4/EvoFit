package com.example.evofit.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class WorkoutUIModel(
    val id: Int,
    val title: String,
    val exercises: Int,
    val series: Int,
    val icon: ImageVector
)