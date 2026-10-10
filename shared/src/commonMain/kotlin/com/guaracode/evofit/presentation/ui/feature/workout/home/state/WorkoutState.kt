package com.guaracode.evofit.presentation.ui.feature.workout.home.state

import androidx.compose.runtime.Immutable
import com.guaracode.evofit.presentation.model.ActiveSessionUIModel
import com.guaracode.evofit.presentation.model.WorkoutHistoryUIModel
import com.guaracode.evofit.presentation.model.WorkoutUIModel

@Immutable
data class WorkoutState(
    val userName: String = "",
    val workouts: List<WorkoutUIModel> = emptyList(),
    val totalWorkouts: Int = 0,
    val workoutsThisWeek: Int = 0,
    val history: List<WorkoutHistoryUIModel> = emptyList(),
    val activeSession: ActiveSessionUIModel? = null,
    val isSyncing: Boolean = false
)
