package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDone(
    val date: String,
    val nameWorkout: String,
    val time: String,
    val muscleGroup: MuscleGroup?,
    val exercises: List<ExerciseSet>
)

data class WorkoutDoneHistory(
    val userId: String,
    val history: List<WorkoutDone>
)
