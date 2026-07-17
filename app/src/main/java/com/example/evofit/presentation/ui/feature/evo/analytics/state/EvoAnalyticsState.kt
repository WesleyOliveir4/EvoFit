package com.example.evofit.presentation.ui.feature.evo.analytics.state

import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.presentation.model.ExerciseWithRecordsUIModel
import com.example.evofit.presentation.model.MuscleGroupItem

data class AnalyticsChartPoint(
    val label: String,
    val value: Float,
    val x: Float = 0f,
    val y: Float = 0f
)

data class EvoAnalyticsState(
    val isLoading: Boolean = false,
    // Cache interno usado só pelo ViewModel para recalcular seleção de grupo/exercício.
    // Nunca deve ser lido diretamente por Composables.
    val historyRawData: List<WorkoutDone> = emptyList(),
    val trainedGroups: List<MuscleGroupItem> = emptyList(),
    val selectedMuscleGroupId: String? = null,
    val muscleGroupName: String = "",
    val exercisesForSelection: List<ExerciseWithRecordsUIModel> = emptyList(),
    val selectedExerciseId: String? = null,
    val selectedExerciseName: String = "",
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
