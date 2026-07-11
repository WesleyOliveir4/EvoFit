package com.example.evofit.presentation.ui.feature.workout.createworkout.state

import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.MeasurementUnit

data class ConfigureWorkoutUiState(
    val exerciseConfigs: List<ExerciseConfigState> = emptyList(),
    val muscleGroupType: MuscleGroupType? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val savedWorkoutId: Long? = null,
    val editWorkoutId: Long? = null
)

data class ExerciseConfigState(
    val exerciseId: String,
    val name: String,
    val muscleGroupId: String,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val sets: List<SetState> = listOf(SetState(1, 20.0, 10))
)

data class SetState(
    val setNumber: Int,
    val weight: Double,
    val reps: Int
)
