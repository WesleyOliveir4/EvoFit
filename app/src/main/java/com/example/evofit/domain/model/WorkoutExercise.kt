package com.example.evofit.domain.model

data class WorkoutExercise(
    val id: Long = 0,
    val exerciseId: String,
    val sets: List<ExerciseSet>
)
