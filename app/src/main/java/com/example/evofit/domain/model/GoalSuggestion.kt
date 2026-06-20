package com.example.evofit.domain.model

data class GoalSuggestion(
    val id: String,
    val text: String,
    val category: ExerciseCategory? = null,
    val exerciseId: String? = null,
    val muscleGroupId: String? = null,
    val isWeightGoal: Boolean = false
)
