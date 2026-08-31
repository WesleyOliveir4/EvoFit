package com.example.evofit.presentation.mapper

import com.example.evofit.R
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.presentation.model.MuscleGroupItem

fun MuscleGroupType.toImageRes(): Int? {
    return when (this) {
        MuscleGroupType.BACK -> R.drawable.ic_back_2
        MuscleGroupType.SHOULDERS -> R.drawable.ic_shoulder_2
        MuscleGroupType.ARMS -> R.drawable.ic_arms_2
        MuscleGroupType.LEGS -> R.drawable.ic_legs_2
        MuscleGroupType.ABS -> R.drawable.ic_core_2
        MuscleGroupType.GLUTES -> R.drawable.ic_gluteus_2
        MuscleGroupType.CALVES -> R.drawable.ic_calf_2
        MuscleGroupType.CHEST -> R.drawable.ic_chest_2
        MuscleGroupType.CARDIO -> R.drawable.ic_cardio_2
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
