package com.example.evofit.presentation.model

data class WorkoutHistoryUIModel(
    val id: String,
    val name: String,
    val date: String,
    val time: String,
    val exercises: List<ExercisePreviewItem>
)

data class ActiveSessionUIModel(
    val workoutId: String,
    val workoutName: String
)
