package com.guaracode.evofit.presentation.model

import org.jetbrains.compose.resources.DrawableResource

data class MuscleGroupItem(
    val id: String,
    val name: String,
    val imageRes: DrawableResource? = null
)
