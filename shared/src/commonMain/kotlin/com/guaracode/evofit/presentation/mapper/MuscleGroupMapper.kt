package com.guaracode.evofit.presentation.mapper

import com.guaracode.evofit.domain.model.MuscleGroup
import com.guaracode.evofit.domain.model.MuscleGroupType
import com.guaracode.evofit.presentation.model.MuscleGroupItem

fun MuscleGroupType.toImageRes(): Int? = null

fun MuscleGroup.toItem(): MuscleGroupItem {
    return MuscleGroupItem(
        id = this.id,
        name = this.name,
        imageRes = null
    )
}
