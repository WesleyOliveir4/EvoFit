package com.example.evofit.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Whatshot
import com.example.evofit.data.model.MuscleGroupType
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.presentation.ui.feature.workout.components.MuscleGroupItem

fun MuscleGroupModel.toItem(): MuscleGroupItem {
    val icon = when (this.type) {
        MuscleGroupType.CHEST -> Icons.Default.Favorite
        MuscleGroupType.BACK -> Icons.Default.Face
        MuscleGroupType.SHOULDERS -> Icons.Default.Face
        MuscleGroupType.ARMS -> Icons.Default.EmojiPeople
        MuscleGroupType.LEGS -> Icons.AutoMirrored.Filled.DirectionsRun
        MuscleGroupType.ABS -> Icons.Default.Whatshot
        MuscleGroupType.CARDIO -> Icons.AutoMirrored.Filled.DirectionsRun
        MuscleGroupType.GLUTES -> Icons.Default.Accessibility
        MuscleGroupType.CALVES -> Icons.AutoMirrored.Filled.DirectionsRun
        MuscleGroupType.OTHER -> Icons.Default.Accessibility
    }
    return MuscleGroupItem(
        id = this.id,
        name = this.name,
        temporaryIcon = icon
    )
}
