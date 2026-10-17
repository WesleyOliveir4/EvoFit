package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.core.common.DateMapper
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

        val monthlyHistory = history
            .filter { it.date.isNotBlank() }
            .mapNotNull { update ->
                val millis = DateMapper.parseDateUtc(update.date)
                if (millis != null) update to millis else null
            }
            .groupBy { (_, millis) ->
                val localDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault()).date
                "${localDate.year}-${localDate.monthNumber}"
            }
            .map { (_, pairs) ->
                pairs.maxBy { it.second }.first
            }
            .sortedBy { update ->
                DateMapper.parseDateUtc(update.date) ?: 0L
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
}
