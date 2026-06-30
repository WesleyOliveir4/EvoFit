package com.example.evofit.domain.model

data class Workout(
    val id: Long = 0,
    val userId: String,
    val name: String,
    val muscleGroupId: String,
    val date: Long,
    val isCompleted: Boolean,
    val exercises: List<WorkoutExercise>
)

data class WorkoutExercise(
    val id: Long = 0,
    val exerciseId: String,
    val sets: List<ExerciseSet>
)

data class ExerciseSet(
    val id: Long = 0,
    val setNumber: Int,
    val reps: Int,
    val load: Double
)
