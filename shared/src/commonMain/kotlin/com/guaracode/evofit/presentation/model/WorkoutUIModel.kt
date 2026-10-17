package com.guaracode.evofit.presentation.model

import org.jetbrains.compose.resources.DrawableResource

data class WorkoutUIModel(
    val id: String,
    val title: String,
    val exercises: Int,
    val series: Int,
    val imageRes: DrawableResource? = null
)
