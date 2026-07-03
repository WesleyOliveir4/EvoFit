package com.example.evofit.data.mapper

import com.example.evofit.data.model.ExerciseModel
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.data.model.MuscleGroupType
import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.MuscleGroupType as DomainMuscleGroupType

fun MuscleGroupModel.toDomain(): MuscleGroup {
    return MuscleGroup(
        id = id,
        name = name,
        type = type.toDomain(),
        category = category
    )
}

fun MuscleGroupType.toDomain(): DomainMuscleGroupType {
    return when (this) {
        MuscleGroupType.CHEST -> DomainMuscleGroupType.CHEST
        MuscleGroupType.BACK -> DomainMuscleGroupType.BACK
        MuscleGroupType.SHOULDERS -> DomainMuscleGroupType.SHOULDERS
        MuscleGroupType.ARMS -> DomainMuscleGroupType.ARMS
        MuscleGroupType.LEGS -> DomainMuscleGroupType.LEGS
        MuscleGroupType.ABS -> DomainMuscleGroupType.ABS
        MuscleGroupType.CARDIO -> DomainMuscleGroupType.CARDIO
        MuscleGroupType.GLUTES -> DomainMuscleGroupType.GLUTES
        MuscleGroupType.CALVES -> DomainMuscleGroupType.CALVES
        MuscleGroupType.OTHER -> DomainMuscleGroupType.OTHER
    }
}

fun ExerciseModel.toDomain(): Exercise {
    return Exercise(
        id = id,
        name = name,
        muscleGroupId = muscleGroupId,
        unit = unit
    )
}

fun MuscleGroup.toData(): MuscleGroupModel {
    return MuscleGroupModel(
        id = id,
        name = name,
        type = type.toData(),
        category = category
    )
}

fun DomainMuscleGroupType.toData(): MuscleGroupType {
    return when (this) {
        DomainMuscleGroupType.CHEST -> MuscleGroupType.CHEST
        DomainMuscleGroupType.BACK -> MuscleGroupType.BACK
        DomainMuscleGroupType.SHOULDERS -> MuscleGroupType.SHOULDERS
        DomainMuscleGroupType.ARMS -> MuscleGroupType.ARMS
        DomainMuscleGroupType.LEGS -> MuscleGroupType.LEGS
        DomainMuscleGroupType.ABS -> MuscleGroupType.ABS
        DomainMuscleGroupType.CARDIO -> MuscleGroupType.CARDIO
        DomainMuscleGroupType.GLUTES -> MuscleGroupType.GLUTES
        DomainMuscleGroupType.CALVES -> MuscleGroupType.CALVES
        DomainMuscleGroupType.OTHER -> MuscleGroupType.OTHER
    }
}
