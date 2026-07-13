package com.example.evofit.presentation.ui.feature.evo.analytics.state

import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.usecase.ExerciseWithRecords

data class AnalyticsChartPoint(
    val label: String,
    val value: Float,
    val x: Float = 0f,
    val y: Float = 0f
)

data class EvoAnalyticsState(
    val isLoading: Boolean = false,
    val historyRawData: List<WorkoutDone> = emptyList(),
    val trainedGroups: List<MuscleGroup> = emptyList(),
    val selectedMuscleGroupId: String? = null,
    val muscleGroupName: String = "",
    val exercisesForSelection: List<ExerciseWithRecords> = emptyList(),
    val selectedExerciseId: String? = null,
    val selectedExerciseName: String = "",
    val maxRecord: String = "-",
    val totalSets: String = "-",
    val firstRecordDate: String = "-",
    val lastRecordDate: String = "-",
    val loadChartPoints: List<AnalyticsChartPoint> = emptyList(),
    val volumeChartPoints: List<AnalyticsChartPoint> = emptyList(),
    val error: String? = null
)
