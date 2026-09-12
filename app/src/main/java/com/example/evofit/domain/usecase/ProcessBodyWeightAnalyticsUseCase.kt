package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.AnalyticsDataPoint
import com.example.evofit.domain.model.ExerciseAnalyticsResult
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WeightUpdate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

interface ProcessBodyWeightAnalyticsUseCase {
    operator fun invoke(history: List<WeightUpdate>): ExerciseAnalyticsResult?
}

class ProcessBodyWeightAnalyticsUseCaseImpl : ProcessBodyWeightAnalyticsUseCase {
    override fun invoke(history: List<WeightUpdate>): ExerciseAnalyticsResult? {
        if (history.isEmpty()) return null

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // 1. Filtrar e agrupar por mês, pegando o último registro de cada mês
        val monthlyHistory = history
            .filter { it.date.isNotBlank() }
            .mapNotNull { update ->
                val date = try { sdf.parse(update.date) } catch (e: Exception) { null }
                if (date != null) update to date else null
            }
            .groupBy { (_, date) ->
                calendar.time = date
                "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}"
            }
            .map { (_, pairs) ->
                // Pega o registro com o maior timestamp (mais recente no mês)
                pairs.maxBy { it.second.time }.first
            }
            .sortedBy { update ->
                try { sdf.parse(update.date)?.time ?: 0L } catch (e: Exception) { 0L }
            }

        if (monthlyHistory.isEmpty()) return null

        // 2. Gerar pontos do gráfico usando formatDateToMonth para labels padronizadas
        val chartPoints = monthlyHistory.map { update ->
            AnalyticsDataPoint(
                label = formatDateToMonth(update.date),
                value = update.weight.replace(",", ".").toFloatOrNull() ?: 0f
            )
        }

        val weights = monthlyHistory.mapNotNull { it.weight.replace(",", ".").toDoubleOrNull() }
        val maxWeight = if (weights.isNotEmpty()) "${weights.maxOrNull()}kg" else "-"
        val minWeight = if (weights.isNotEmpty()) "${weights.minOrNull()}kg" else "-"

        return ExerciseAnalyticsResult(
            unit = MeasurementUnit.WEIGHT,
            maxRecord = maxWeight,
            secondaryRecord = minWeight,
            totalSets = history.size.toString(), // Total de registros reais
            firstRecordDate = formatDate(monthlyHistory.first().date),
            lastRecordDate = formatDate(monthlyHistory.last().date),
            loadChartPoints = chartPoints
        )
    }
}
