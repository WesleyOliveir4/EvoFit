package com.example.evofit.presentation.ui.feature.workout.home.state

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.presentation.model.WorkoutUIModel

data class WorkoutState(
    val userName: String = "",
    val workouts: List<WorkoutUIModel> = emptyList(),
    val totalWorkouts: Int = 0,
    val workoutsThisWeek: Int = 0,
    val history: List<WorkoutDone> = emptyList()
)
