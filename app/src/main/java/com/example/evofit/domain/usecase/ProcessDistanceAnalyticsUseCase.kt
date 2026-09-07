package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.AnalyticsDataPoint
import com.example.evofit.domain.model.ExerciseAnalyticsResult
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone
import java.util.Locale

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
            // Pega todos os sets do exercício específico em todos os treinos desse mês
            val monthSets = workouts.flatMap { w -> 
                w.exercisesByGroup.flatMap { g -> g.exercises }
                    .filter { it.exerciseId == exerciseId }
                    .flatMap { it.sets }
            }.filter { (it.distance ?: 0.0) > 0 }
            
            if (monthSets.isEmpty()) return@forEach

            totalSetsCount += monthSets.size
            val label = formatDateToMonth(workouts.first().date)

            // Recorde de distância: maior distância em uma única série
            val monthMaxDistance = monthSets.maxOfOrNull { it.distance ?: 0.0 } ?: 0.0
            if (monthMaxDistance > globalMaxDistance) globalMaxDistance = monthMaxDistance

            // Eixo Primário: Média de distância por série no mês
            val avgDistance = monthSets.mapNotNull { it.distance }.average().takeIf { !it.isNaN() } ?: 0.0
            primaryChartPoints.add(AnalyticsDataPoint(label, avgDistance.toFloat()))

            // Eixo Secundário: Velocidade média mensal calculada série por série
            val setSpeeds = monthSets.mapNotNull { set ->
                val d = set.distance ?: 0.0
                val t = (set.time ?: 0).toDouble()
                
                // Cálculo: Distância (km) / (Tempo em minutos / 60.0) -> km/h
                // Usamos 60.0 porque o input do usuário (30) representa 30 minutos
                // Exemplo: 4km / (30/60) = 8km/h
                if (t > 0) (d / (t / 60.0)) else null
            }
            
            val monthAvgSpeed = if (setSpeeds.isNotEmpty()) setSpeeds.average() else 0.0
            
            // O recorde secundário (Velocidade Média) será o maior valor médio mensal atingido
            if (monthAvgSpeed > maxMonthlyAvgSpeed) {
                maxMonthlyAvgSpeed = monthAvgSpeed
            }
            
            secondaryChartPoints.add(AnalyticsDataPoint(label, monthAvgSpeed.toFloat()))
        }

        val secondaryRecordStr = String.format(Locale.US, "%.1f km/h", maxMonthlyAvgSpeed)

        return ExerciseAnalyticsResult(
            unit = MeasurementUnit.DISTANCE,
            maxRecord = String.format(Locale.US, "%.2fkm", globalMaxDistance),
            secondaryRecord = secondaryRecordStr,
            totalSets = totalSetsCount.toString(),
            firstRecordDate = formatDate(filteredWorkouts.first().date),
            lastRecordDate = formatDate(filteredWorkouts.last().date),
            loadChartPoints = primaryChartPoints,
            volumeChartPoints = secondaryChartPoints
        )
    }
}
