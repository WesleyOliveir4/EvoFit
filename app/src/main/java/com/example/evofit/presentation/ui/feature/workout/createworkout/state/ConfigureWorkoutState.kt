package com.example.evofit.presentation.ui.feature.workout.createworkout.state

import androidx.compose.runtime.Immutable
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.MeasurementUnit

@Immutable
data class ConfigureWorkoutUiState(
    val exerciseConfigs: List<ExerciseConfigState> = emptyList(),
    val muscleGroupType: MuscleGroupType? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val savedWorkoutId: String? = null,
    val editWorkoutId: String? = null
)

@Immutable
data class ExerciseConfigState(
    val workoutExerciseId: String = "",
    val exerciseId: String,
    val name: String,
    val muscleGroupId: String,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val sets: List<SetState> = emptyList()
)

@Immutable
data class SetState(
    val id: String = "",
    val setNumber: Int,
    val weight: Double,
    val reps: Int
)
