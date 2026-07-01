package com.example.evofit.presentation.ui.feature.workout.state

import com.example.evofit.presentation.model.WorkoutUIModel

data class WorkoutState(
    val userName: String = "",
    val workouts: List<WorkoutUIModel> = emptyList(),
    val totalWorkouts: Int = 0,
    val workoutsThisWeek: Int = 0
)