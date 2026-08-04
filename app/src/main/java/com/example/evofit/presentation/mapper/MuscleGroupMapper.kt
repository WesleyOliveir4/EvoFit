package com.example.evofit.presentation.mapper

import com.example.evofit.R
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.presentation.model.MuscleGroupItem

fun MuscleGroupType.toImageRes(): Int? {
    return when (this) {
        MuscleGroupType.BACK -> R.drawable.img_back
        MuscleGroupType.SHOULDERS -> R.drawable.img_shoulder
        MuscleGroupType.ARMS -> R.drawable.img_arms
        MuscleGroupType.LEGS -> R.drawable.img_legs
        MuscleGroupType.ABS -> R.drawable.img_abs
        MuscleGroupType.GLUTES -> R.drawable.img_gluteus
        MuscleGroupType.CALVES -> R.drawable.img_calf
        MuscleGroupType.CHEST -> R.drawable.img_chest
        MuscleGroupType.CARDIO -> R.drawable.img_cardio
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
