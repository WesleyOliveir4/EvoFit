package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.AnalyticsDataPoint
import com.example.evofit.domain.model.ExerciseAnalyticsResult
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone

interface ProcessRepsAnalyticsUseCase {
    operator fun invoke(exerciseId: String, filteredWorkouts: List<WorkoutDone>): ExerciseAnalyticsResult
}

class ProcessRepsAnalyticsUseCaseImpl : ProcessRepsAnalyticsUseCase {
    override fun invoke(exerciseId: String, filteredWorkouts: List<WorkoutDone>): ExerciseAnalyticsResult {
        val primaryChartPoints = mutableListOf<AnalyticsDataPoint>()
        var globalMaxRecord = 0.0
        var totalSetsCount = 0

        groupWorkoutsByMonth(filteredWorkouts).forEach { (_, workouts) ->
            val monthSets = workouts.flatMap { w -> 
                w.exercisesByGroup.flatMap { g -> g.exercises }
                    .filter { it.exerciseId == exerciseId }
                    .flatMap { it.sets }
            }
            totalSetsCount += monthSets.size
            val label = formatDateToMonth(workouts.first().date)

            val monthMaxReps = monthSets.maxOfOrNull { it.reps }?.toDouble() ?: 0.0
            if (monthMaxReps > globalMaxRecord) globalMaxRecord = monthMaxReps

            val avgReps = monthSets.map { it.reps }.average().takeIf { !it.isNaN() } ?: 0.0
            primaryChartPoints.add(AnalyticsDataPoint(label, avgReps.toFloat()))
        }

        return ExerciseAnalyticsResult(
            unit = MeasurementUnit.REPS,
            maxRecord = "${globalMaxRecord.toInt()} reps",
            secondaryRecord = null,
            totalSets = totalSetsCount.toString(),
            firstRecordDate = formatDate(filteredWorkouts.first().date),
            lastRecordDate = formatDate(filteredWorkouts.last().date),
            loadChartPoints = primaryChartPoints,
            volumeChartPoints = emptyList()
        )
    }
}
