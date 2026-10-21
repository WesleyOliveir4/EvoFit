package com.guaracode.evofit.presentation.mapper

import com.guaracode.evofit.domain.model.MuscleGroup
import com.guaracode.evofit.domain.model.MuscleGroupType
import com.guaracode.evofit.presentation.model.MuscleGroupItem
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_back
import evofit.shared.generated.resources.ic_biceps
import evofit.shared.generated.resources.ic_calf
import evofit.shared.generated.resources.ic_cardio
import evofit.shared.generated.resources.ic_chest
import evofit.shared.generated.resources.ic_core
import evofit.shared.generated.resources.ic_forearm
import evofit.shared.generated.resources.ic_gluteus
import evofit.shared.generated.resources.ic_legs_2
import evofit.shared.generated.resources.ic_shoulder
import evofit.shared.generated.resources.ic_triceps_2
import org.jetbrains.compose.resources.DrawableResource

fun MuscleGroupType.toImageRes(): DrawableResource? {
    return when (this) {
        MuscleGroupType.BACK -> Res.drawable.ic_back
        MuscleGroupType.SHOULDERS -> Res.drawable.ic_shoulder
        MuscleGroupType.BICEPS -> Res.drawable.ic_biceps
        MuscleGroupType.TRICEPS -> Res.drawable.ic_triceps_2
        MuscleGroupType.FOREARMS -> Res.drawable.ic_forearm
        MuscleGroupType.LEGS -> Res.drawable.ic_legs_2
        MuscleGroupType.ABS -> Res.drawable.ic_core
        MuscleGroupType.GLUTES -> Res.drawable.ic_gluteus
        MuscleGroupType.CALVES -> Res.drawable.ic_calf
        MuscleGroupType.CHEST -> Res.drawable.ic_chest
        MuscleGroupType.CARDIO -> Res.drawable.ic_cardio
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
