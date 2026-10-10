package com.guaracode.evofit.presentation.ui.feature.evo.analytics.state

import androidx.compose.runtime.Immutable
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.presentation.model.ExerciseWithRecordsUIModel
import com.guaracode.evofit.presentation.model.MuscleGroupItem

@Immutable
data class AnalyticsChartPoint(
    val label: String,
    val value: Float,
    val x: Float = 0f,
    val y: Float = 0f
)

@Immutable
data class EvoAnalyticsState(
    val isLoading: Boolean = false,
    val historyRawData: List<WorkoutDone> = emptyList(),
    val trainedGroups: List<MuscleGroupItem> = emptyList(),
    val selectedMuscleGroupId: String? = null,
    val muscleGroupName: String = "",
    val exercisesForSelection: List<ExerciseWithRecordsUIModel> = emptyList(),
    val selectedExerciseId: String? = null,
    val selectedExerciseName: String = "",
    val isWeightAnalysis: Boolean = false,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val maxRecord: String = "-",
    val secondaryRecord: String? = null,
    val totalSets: String = "-",
    val firstRecordDate: String = "-",
    val lastRecordDate: String = "-",
    val loadChartPoints: List<AnalyticsChartPoint> = emptyList(),
    val volumeChartPoints: List<AnalyticsChartPoint> = emptyList(),
    val error: String? = null
)
