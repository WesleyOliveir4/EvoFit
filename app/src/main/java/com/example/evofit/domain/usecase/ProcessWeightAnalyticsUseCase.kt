package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.AnalyticsDataPoint
import com.example.evofit.domain.model.ExerciseAnalyticsResult
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone

interface ProcessWeightAnalyticsUseCase {
    operator fun invoke(exerciseId: String, filteredWorkouts: List<WorkoutDone>): ExerciseAnalyticsResult
}

class ProcessWeightAnalyticsUseCaseImpl : ProcessWeightAnalyticsUseCase {
    override fun invoke(exerciseId: String, filteredWorkouts: List<WorkoutDone>): ExerciseAnalyticsResult {
        val primaryChartPoints = mutableListOf<AnalyticsDataPoint>()
        val secondaryChartPoints = mutableListOf<AnalyticsDataPoint>()
        var globalMaxRecord = 0.0
        var totalSetsCount = 0

        groupWorkoutsByMonth(filteredWorkouts).forEach { (_, workouts) ->
            val monthSets = workouts.flatMap { w -> w.exercises.first { it.exerciseId == exerciseId }.sets }
            totalSetsCount += monthSets.size
            val label = formatDateToMonth(workouts.first().date)

            val monthMaxLoad = monthSets.maxOfOrNull { it.load } ?: 0.0
            val monthVolume = monthSets.sumOf { it.load * it.reps }

            if (monthMaxLoad > globalMaxRecord) globalMaxRecord = monthMaxLoad

            primaryChartPoints.add(AnalyticsDataPoint(label, monthMaxLoad.toFloat()))
            secondaryChartPoints.add(AnalyticsDataPoint(label, monthVolume.toFloat()))
        }

        return ExerciseAnalyticsResult(
            unit = MeasurementUnit.WEIGHT,
            maxRecord = "${globalMaxRecord.toInt()}kg",
            secondaryRecord = null,
            totalSets = totalSetsCount.toString(),
            firstRecordDate = formatDate(filteredWorkouts.first().date),
            lastRecordDate = formatDate(filteredWorkouts.last().date),
            loadChartPoints = primaryChartPoints,
            volumeChartPoints = secondaryChartPoints
        )
    }
}
