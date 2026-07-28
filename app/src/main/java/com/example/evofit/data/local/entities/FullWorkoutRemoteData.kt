package com.example.evofit.data.local.entities

data class FullWorkoutRemoteData(
    val workout: WorkoutEntity,
    val exercises: List<WorkoutExerciseEntity>,
    val sets: List<List<ExerciseSetEntity>>
)
