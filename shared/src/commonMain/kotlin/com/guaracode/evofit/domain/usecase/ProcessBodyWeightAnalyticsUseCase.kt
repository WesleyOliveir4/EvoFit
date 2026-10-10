package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.AnalyticsDataPoint
import com.guaracode.evofit.domain.model.ExerciseAnalyticsResult
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WeightUpdate
import kotlinx.datetime.*

interface ProcessBodyWeightAnalyticsUseCase {
    operator fun invoke(history: List<WeightUpdate>): ExerciseAnalyticsResult?
}

class ProcessBodyWeightAnalyticsUseCaseImpl : ProcessBodyWeightAnalyticsUseCase {
    override fun invoke(history: List<WeightUpdate>): ExerciseAnalyticsResult? {
        if (history.isEmpty()) return null

        val parsedHistory = history
            .filter { it.date.isNotBlank() }
            .mapNotNull { update ->
                val millis = parseDateToMillis(update.date)
                if (millis > 0) update to millis else null
            }

        if (parsedHistory.isEmpty()) return null

        val monthlyHistory = parsedHistory
            .groupBy { (_, millis) ->
                val instant = Instant.fromEpochMilliseconds(millis)
                val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                "${dt.year}-${dt.monthNumber.toString().padStart(2, '0')}"
            }
            .map { (_, pairs) ->
                pairs.maxBy { it.second }.first
            }
            .sortedBy { update ->
                parseDateToMillis(update.date)
            }

        if (monthlyHistory.isEmpty()) return null

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
            totalSets = history.size.toString(),
            firstRecordDate = formatDate(monthlyHistory.first().date),
            lastRecordDate = formatDate(monthlyHistory.last().date),
            loadChartPoints = chartPoints
        )
    }

    private fun parseDateToMillis(dateStr: String): Long {
        return try {
            val parts = if (dateStr.contains("/")) dateStr.split("/") else dateStr.split("-")
            if (parts.size == 3) {
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2].toInt()
                LocalDate(year, month, day).atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            } else 0L
        } catch (e: Exception) {
            0L
        }
    }
}
