package com.example.evofit.presentation.ui.feature.workout.startworkout.session

import androidx.compose.runtime.Immutable
import com.example.evofit.domain.model.MeasurementUnit

@Immutable
data class WorkoutStartUiState(
    val workoutTitle: String = "",
    val exercises: List<ExerciseProgressState> = emptyList(),
    val isLoading: Boolean = true,
    val elapsedTime: String = "00:00:00",
    val showFinishDialog: Boolean = false,
    val showCancelDialog: Boolean = false,
    val workoutCompleted: Boolean = false,
    val workoutNotFinished: Boolean = false,
    val workoutDoneId: String? = null
)

@Immutable
data class ExerciseProgressState(
    val workoutExerciseId: String,
    val exerciseId: String,
    val name: String,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val sets: List<SetProgressState>
)

@Immutable
data class SetProgressState(
    val setNumber: Int,
    val weight: Double,
    val reps: Int,
    val time: Int? = null,
    val distance: Double? = null,
    val isDone: Boolean = false
)
