package com.guaracode.evofit.presentation.mapper

import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_logo_evofit
import org.jetbrains.compose.resources.DrawableResource

object ExerciseMapper {
    fun toImageRes(exerciseId: String): DrawableResource {
        return Res.drawable.ic_logo_evofit
    }
}
