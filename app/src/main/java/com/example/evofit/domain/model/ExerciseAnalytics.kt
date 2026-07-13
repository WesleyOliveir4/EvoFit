package com.example.evofit.domain.model

data class AnalyticsDataPoint(
    val label: String,
    val value: Float
)

data class ExerciseAnalyticsResult(
    val unit: MeasurementUnit,
    val maxRecord: String,
    val secondaryRecord: String? = null,
    val totalSets: String,
    val firstRecordDate: String,
    val lastRecordDate: String,
    val loadChartPoints: List<AnalyticsDataPoint>,
    val volumeChartPoints: List<AnalyticsDataPoint> = emptyList()
)
