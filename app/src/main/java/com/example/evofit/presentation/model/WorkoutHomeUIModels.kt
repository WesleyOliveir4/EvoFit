package com.example.evofit.presentation.model

data class WorkoutHistoryUIModel(
    val id: Long,
    val name: String,
    val date: String,
    val time: String,
    val exercises: List<ExercisePreviewItem>
)

data class ActiveSessionUIModel(
    val workoutId: Int,
    val workoutName: String
)
