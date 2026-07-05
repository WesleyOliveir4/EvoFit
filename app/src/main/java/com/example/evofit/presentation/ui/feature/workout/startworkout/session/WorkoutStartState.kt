package com.example.evofit.presentation.ui.feature.workout.startworkout.session

import com.example.evofit.domain.model.MeasurementUnit

data class WorkoutStartUiState(
    val workoutTitle: String = "",
    val exercises: List<ExerciseProgressState> = emptyList(),
    val isLoading: Boolean = true,
    val elapsedTime: String = "00:00:00",
    val showFinishDialog: Boolean = false,
    val workoutCompleted: Boolean = false
)

data class ExerciseProgressState(
    val workoutExerciseId: Long,
    val exerciseId: String,
    val name: String,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val sets: List<SetProgressState>
)

data class SetProgressState(
    val setNumber: Int,
    val weight: Double,
    val reps: Int,
    val time: Int? = null,
    val distance: Double? = null,
    val isDone: Boolean = false
)
