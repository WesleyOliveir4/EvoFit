package com.example.evofit.domain.model

import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.data.local.entities.ExerciseSetEntity
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDone(
    val date: String,
    val nameWorkout: String,
    val time: String,
    val muscleGroupModel: MuscleGroupModel?,
    val exercises: List<ExerciseSetEntity>
)

data class WorkoutDoneHistory(
    val userId: String,
    val history: List<WorkoutDone>
)
