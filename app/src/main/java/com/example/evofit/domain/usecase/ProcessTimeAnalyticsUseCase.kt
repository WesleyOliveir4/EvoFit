package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.AnalyticsDataPoint
import com.example.evofit.domain.model.ExerciseAnalyticsResult
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone

interface ProcessTimeAnalyticsUseCase {
    operator fun invoke(exerciseId: String, filteredWorkouts: List<WorkoutDone>): ExerciseAnalyticsResult
}

class ProcessTimeAnalyticsUseCaseImpl : ProcessTimeAnalyticsUseCase {
    override fun invoke(exerciseId: String, filteredWorkouts: List<WorkoutDone>): ExerciseAnalyticsResult {
        val primaryChartPoints = mutableListOf<AnalyticsDataPoint>()
        var globalMaxRecord = 0.0
        var totalSetsCount = 0

        groupWorkoutsByMonth(filteredWorkouts).forEach { (_, workouts) ->
            val monthSets = workouts.flatMap { w -> w.exercises.first { it.exerciseId == exerciseId }.sets }
            totalSetsCount += monthSets.size
            val label = formatDateToMonth(workouts.first().date)

            val monthMaxTime = monthSets.maxOfOrNull { it.time ?: 0 }?.toDouble() ?: 0.0
            if (monthMaxTime > globalMaxRecord) globalMaxRecord = monthMaxTime

            val avgTime = monthSets.mapNotNull { it.time }.average().takeIf { !it.isNaN() } ?: 0.0
            primaryChartPoints.add(AnalyticsDataPoint(label, (avgTime / 60.0).toFloat()))
        }

        return ExerciseAnalyticsResult(
            unit = MeasurementUnit.TIME,
            maxRecord = formatSecondsToTime(globalMaxRecord.toInt()),
            secondaryRecord = null,
            totalSets = totalSetsCount.toString(),
            firstRecordDate = formatDate(filteredWorkouts.first().date),
            lastRecordDate = formatDate(filteredWorkouts.last().date),
            loadChartPoints = primaryChartPoints,
            volumeChartPoints = emptyList()
        )
    }
}
