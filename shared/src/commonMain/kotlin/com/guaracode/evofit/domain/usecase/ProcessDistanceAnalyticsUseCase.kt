package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.AnalyticsDataPoint
import com.guaracode.evofit.domain.model.ExerciseAnalyticsResult
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WorkoutDone

interface ProcessDistanceAnalyticsUseCase {
    operator fun invoke(exerciseId: String, filteredWorkouts: List<WorkoutDone>): ExerciseAnalyticsResult
}

class ProcessDistanceAnalyticsUseCaseImpl : ProcessDistanceAnalyticsUseCase {
    override fun invoke(exerciseId: String, filteredWorkouts: List<WorkoutDone>): ExerciseAnalyticsResult {
        val primaryChartPoints = mutableListOf<AnalyticsDataPoint>()
        val secondaryChartPoints = mutableListOf<AnalyticsDataPoint>()
        var globalMaxDistance = 0.0
        var totalSetsCount = 0
        var maxMonthlyAvgSpeed = 0.0

        groupWorkoutsByMonth(filteredWorkouts).forEach { (_, workouts) ->
            val monthSets = workouts.flatMap { w -> 
                w.exercisesByGroup.flatMap { g -> g.exercises }
                    .filter { it.exerciseId == exerciseId }
                    .flatMap { it.sets }
            }.filter { (it.distance ?: 0.0) > 0 }
            
            if (monthSets.isEmpty()) return@forEach

            totalSetsCount += monthSets.size
            val label = formatDateToMonth(workouts.first().date)

            val monthMaxDistance = monthSets.maxOfOrNull { it.distance ?: 0.0 } ?: 0.0
            if (monthMaxDistance > globalMaxDistance) globalMaxDistance = monthMaxDistance

            val avgDistance = monthSets.mapNotNull { it.distance }.average().takeIf { !it.isNaN() } ?: 0.0
            primaryChartPoints.add(AnalyticsDataPoint(label, avgDistance.toFloat()))

            val setSpeeds = monthSets.mapNotNull { set ->
                val d = set.distance ?: 0.0
                val t = (set.time ?: 0).toDouble()
                if (t > 0) (d / (t / 60.0)) else null
            }
            
            val monthAvgSpeed = if (setSpeeds.isNotEmpty()) setSpeeds.average() else 0.0
            
            if (monthAvgSpeed > maxMonthlyAvgSpeed) {
                maxMonthlyAvgSpeed = monthAvgSpeed
            }
            
            secondaryChartPoints.add(AnalyticsDataPoint(label, monthAvgSpeed.toFloat()))
        }

        val roundedSpeed = ((maxMonthlyAvgSpeed * 10).toInt() / 10.0)
        val secondaryRecordStr = "$roundedSpeed km/h"

        val roundedDist = ((globalMaxDistance * 100).toInt() / 100.0)
        val maxRecordStr = "${roundedDist}km"

        return ExerciseAnalyticsResult(
            unit = MeasurementUnit.DISTANCE,
            maxRecord = maxRecordStr,
            secondaryRecord = secondaryRecordStr,
            totalSets = totalSetsCount.toString(),
            firstRecordDate = formatDate(filteredWorkouts.first().date),
            lastRecordDate = formatDate(filteredWorkouts.last().date),
            loadChartPoints = primaryChartPoints,
            volumeChartPoints = secondaryChartPoints
        )
    }
}
