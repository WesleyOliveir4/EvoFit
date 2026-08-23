package com.example.evofit.presentation.mapper

import com.example.evofit.R
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.presentation.model.MuscleGroupItem

fun MuscleGroupType.toImageRes(): Int? {
    return when (this) {
        MuscleGroupType.BACK -> R.drawable.ic_back
        MuscleGroupType.SHOULDERS -> R.drawable.ic_shoulder
        MuscleGroupType.ARMS -> R.drawable.ic_arms
        MuscleGroupType.LEGS -> R.drawable.ic_legs
        MuscleGroupType.ABS -> R.drawable.ic_legs
        MuscleGroupType.GLUTES -> R.drawable.ic_gluteus
        MuscleGroupType.CALVES -> R.drawable.ic_calf
        MuscleGroupType.CHEST -> R.drawable.ic_chest
        MuscleGroupType.CARDIO -> R.drawable.ic_cardio
        else -> null
    }
}

fun MuscleGroup.toItem(): MuscleGroupItem {
    return MuscleGroupItem(
        id = this.id,
        name = this.name,
        imageRes = this.type.toImageRes()
    )
}
