package com.guaracode.evofit.presentation.mapper

import com.guaracode.evofit.domain.model.MuscleGroupType
import com.guaracode.evofit.domain.model.MuscleGroup
import com.guaracode.evofit.presentation.model.MuscleGroupItem
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_logo_evofit
import org.jetbrains.compose.resources.DrawableResource

fun MuscleGroupType.toImageRes(): DrawableResource {
    return Res.drawable.ic_logo_evofit
}

fun MuscleGroup.toItem(): MuscleGroupItem {
    return MuscleGroupItem(
        id = this.id,
        name = this.name,
        imageRes = this.type.toImageRes()
    )
}
