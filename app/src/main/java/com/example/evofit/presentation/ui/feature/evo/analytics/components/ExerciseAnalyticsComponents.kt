package com.example.evofit.presentation.ui.feature.evo.analytics.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.ui.feature.evo.analytics.state.AnalyticsChartPoint
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.evoColors
import androidx.compose.ui.platform.LocalConfiguration
import java.util.Locale


private data class YAxisConfig(val min: Float, val max: Float, val step: Float)

private fun calculateYAxisConfig(points: List<AnalyticsChartPoint>): YAxisConfig {
    val maxVal = points.maxOfOrNull { it.value } ?: 50f
    val targetMax = if (maxVal <= 0) 50f else maxVal * 1.25f
    
    val step = when {
        targetMax <= 50 -> 10f
        targetMax <= 100 -> 20f
        targetMax <= 250 -> 50f
        targetMax <= 500 -> 100f
        targetMax <= 1000 -> 200f
        targetMax <= 2500 -> 500f
        targetMax <= 5000 -> 1000f
        targetMax <= 10000 -> 2000f
        else -> (targetMax / 5).coerceAtLeast(1000f)
    }
    
    val finalMax = (kotlin.math.ceil(targetMax / step) * step).coerceAtLeast(step * 4).toFloat()
    return YAxisConfig(0f, finalMax, step)
}

@Composable
fun MetricStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color? = null,
    modifier: Modifier = Modifier
) {
    val displayColor = iconColor ?: MaterialTheme.evoColors.green

    Card(
        modifier = modifier.height(Dimens.EvoCardHeightSmall),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = displayColor,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraExtraSmall)) {
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
fun EvoExerciseChart(
    isCargaSelected: Boolean,
    unit: MeasurementUnit,
    points: List<AnalyticsChartPoint>,
    onTabChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeColor = if (isCargaSelected) MaterialTheme.evoColors.green else MaterialTheme.evoColors.purple
    
    val locale = LocalConfiguration.current.locales[0]
    val theme = MaterialTheme.colorScheme

    var selectedIndex by remember(points) {
        mutableStateOf(if (points.isNotEmpty()) points.size - 1 else -1)
    }

    val showTabs = unit == MeasurementUnit.WEIGHT || unit == MeasurementUnit.DISTANCE

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = theme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge)
        ) {
            if (showTabs) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val primaryTabLabel = when (unit) {
                        MeasurementUnit.WEIGHT -> stringResource(R.string.analytics_chart_tab_load)
                        MeasurementUnit.DISTANCE -> stringResource(R.string.analytics_chart_tab_distance)
                        else -> ""
                    }
                    val secondaryTabLabel = when (unit) {
                        MeasurementUnit.WEIGHT -> stringResource(R.string.analytics_chart_tab_volume)
                        MeasurementUnit.DISTANCE -> stringResource(R.string.analytics_chart_tab_speed)
                        else -> ""
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.SpacingMedium))
                            .background(if (isCargaSelected) theme.primaryContainer.copy(alpha = 0.5f) else Color.Transparent)
                            .clickable { onTabChanged(true) }
                            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall)
                    ) {
                        Text(
                            text = primaryTabLabel,
                            color = if (isCargaSelected) MaterialTheme.evoColors.green else theme.secondary,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.SpacingMedium))
                            .background(if (!isCargaSelected) theme.tertiaryContainer.copy(alpha = 0.5f) else Color.Transparent)
                            .clickable { onTabChanged(false) }
                            .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall)
                    ) {
                        Text(
                            text = secondaryTabLabel,
                            color = if (!isCargaSelected) MaterialTheme.evoColors.purple else theme.secondary,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            }

            val selectedPoint = if (selectedIndex in points.indices) points[selectedIndex] else null
            
            Text(
                text = if (selectedPoint != null) {
                    stringResource(R.string.analytics_chart_average_label, selectedPoint.label)
                } else {
                    stringResource(R.string.analytics_chart_empty_history)
                },
                color = theme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
            
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraExtraSmall)
            ) {
                val formattedValue = when (unit) {
                    MeasurementUnit.TIME -> {
                        val totalMinutes = selectedPoint?.value ?: 0f
                        val hours = (totalMinutes / 60).toInt()
                        val minutes = (totalMinutes % 60).toInt()
                        if (hours > 0) {
                            stringResource(R.string.analytics_unit_hours_format, hours, minutes)
                        } else {
                            stringResource(R.string.analytics_unit_minutes_format, minutes)
                        }
                    }
                    else -> {
                        if (isCargaSelected || unit == MeasurementUnit.REPS) {
                            selectedPoint?.value?.toInt()?.toString() ?: "0"
                        } else {
                            String.format(locale, "%.1f", selectedPoint?.value ?: 0f)
                        }
                    }
                }

                Text(
                    text = formattedValue,
                    color = theme.onSurface,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp)
                )

                val unitLabel = when (unit) {
                    MeasurementUnit.WEIGHT -> if (isCargaSelected) stringResource(R.string.analytics_unit_kg) else stringResource(R.string.analytics_unit_vol)
                    MeasurementUnit.DISTANCE -> if (isCargaSelected) stringResource(R.string.analytics_unit_km) else stringResource(R.string.analytics_unit_kmh)
                    MeasurementUnit.TIME -> ""
                    MeasurementUnit.REPS -> stringResource(R.string.analytics_unit_reps)
                }

                if (unitLabel.isNotEmpty()) {
                    Text(
                        text = unitLabel,
                        color = theme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = Dimens.SpacingTiny)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            val scrollState = rememberScrollState()
            val itemWidth = Dimens.EvoGraphItemWidth 
            val labelColor = theme.secondary.toArgb()
            val isScrollable = points.size > 5
            
            val yAxisConfig = calculateYAxisConfig(points)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.EvoGraphHeight)
            ) {
                Canvas(modifier = Modifier
                    .fillMaxHeight()
                    .width(Dimens.EvoGraphYAxisWidth)
                    .padding(vertical = Dimens.SpacingLarge)
                ) {
                    val canvasHeight = size.height
                    val steps = ((yAxisConfig.max - yAxisConfig.min) / yAxisConfig.step).toInt()
                    val paint = android.graphics.Paint().apply {
                        color = labelColor
                        textSize = 10.sp.toPx()
                        textAlign = android.graphics.Paint.Align.RIGHT
                        isAntiAlias = true
                    }

                    for (i in 0..steps) {
                        val value = yAxisConfig.min + (i * yAxisConfig.step)
                        if (value > yAxisConfig.max) break
                        
                        val y = canvasHeight - ((value - yAxisConfig.min) / (yAxisConfig.max - yAxisConfig.min) * canvasHeight)
                        
                        drawContext.canvas.nativeCanvas.drawText(
                            value.toInt().toString(),
                            size.width - Dimens.SpacingTiny.toPx(),
                            y + (paint.textSize / 3),
                            paint
                        )
                    }
                }

                val scrollModifier = if (isScrollable) Modifier.horizontalScroll(scrollState) else Modifier
                val contentWidthModifier = if (isScrollable) Modifier.width(itemWidth * points.size) else Modifier.fillMaxWidth()

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .then(scrollModifier)
                ) {
                    if (points.isNotEmpty()) {
                        Canvas(modifier = Modifier
                            .then(contentWidthModifier)
                            .fillMaxHeight()
                            .padding(vertical = Dimens.SpacingLarge)
                            .pointerInput(points) {
                                detectTapGestures { offset ->
                                    val xPos = offset.x
                                    val canvasWidth = size.width
                                    if (canvasWidth > 0) {
                                        val index = (xPos / (canvasWidth / points.size)).toInt().coerceIn(points.indices)
                                        selectedIndex = index
                                    }
                                }
                            }
                        ) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            val stepX = canvasWidth / points.size
                            
                            val steps = ((yAxisConfig.max - yAxisConfig.min) / yAxisConfig.step).toInt()
                            for (i in 0..steps) {
                                val value = yAxisConfig.min + (i * yAxisConfig.step)
                                if (value > yAxisConfig.max) break
                                val y = canvasHeight - ((value - yAxisConfig.min) / (yAxisConfig.max - yAxisConfig.min) * canvasHeight)
                                
                                drawLine(
                                    color = theme.surfaceVariant,
                                    start = Offset(0f, y),
                                    end = Offset(canvasWidth, y),
                                    strokeWidth = Dimens.BorderWidthThin.toPx() / 2
                                )
                            }

                            val drawPoints = points.mapIndexed { index, point ->
                                val x = (index + 0.5f) * stepX
                                val clampedValue = point.value.coerceIn(yAxisConfig.min, yAxisConfig.max)
                                val y = canvasHeight - ((clampedValue - yAxisConfig.min) / (yAxisConfig.max - yAxisConfig.min) * canvasHeight)
                                Offset(x, y)
                            }

                            if (selectedIndex in drawPoints.indices) {
                                val selectedX = drawPoints[selectedIndex].x
                                val barWidth = if (points.size == 1) stepX * 0.5f else stepX * 0.85f
                                drawRoundRect(
                                    color = activeColor.copy(alpha = 0.1f),
                                    topLeft = Offset(selectedX - (barWidth / 2), 0f),
                                    size = androidx.compose.ui.geometry.Size(barWidth, canvasHeight),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(Dimens.SpacingMediumSmall.toPx(), Dimens.SpacingMediumSmall.toPx())
                                )
                            }

                            if (drawPoints.isNotEmpty()) {
                                val strokePath = Path().apply {
                                    if (drawPoints.size == 1) {
                                        val p = drawPoints[0]
                                        moveTo(0f, p.y)
                                        lineTo(canvasWidth, p.y)
                                    } else {
                                        moveTo(0f, drawPoints.first().y)
                                        lineTo(drawPoints.first().x, drawPoints.first().y)
                                        
                                        for (i in 0 until drawPoints.size - 1) {
                                            val p1 = drawPoints[i]
                                            val p2 = drawPoints[i + 1]
                                            cubicTo(
                                                (p1.x + p2.x) / 2, p1.y,
                                                (p1.x + p2.x) / 2, p2.y,
                                                p2.x, p2.y
                                            )
                                        }
                                        lineTo(canvasWidth, drawPoints.last().y)
                                    }
                                }

                                val fillPath = Path().apply {
                                    if (drawPoints.size == 1) {
                                        val p = drawPoints[0]
                                        moveTo(0f, p.y)
                                        lineTo(canvasWidth, p.y)
                                        lineTo(canvasWidth, canvasHeight)
                                        lineTo(0f, canvasHeight)
                                    } else {
                                        addPath(strokePath)
                                        lineTo(canvasWidth, canvasHeight)
                                        lineTo(0f, canvasHeight)
                                    }
                                    close()
                                }
                                
                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(activeColor.copy(alpha = 0.3f), Color.Transparent),
                                        endY = canvasHeight
                                    )
                                )

                                drawPath(
                                    path = strokePath,
                                    color = activeColor,
                                    style = Stroke(width = Dimens.SpacingExtraExtraSmall.toPx() + 1.dp.toPx())
                                )
                            }

                            drawPoints.forEachIndexed { index, pt ->
                                val isSelected = index == selectedIndex
                                drawCircle(
                                    color = if (isSelected) Color.White else theme.background,
                                    radius = if (isSelected) Dimens.SpacingTiny.toPx() else Dimens.SpacingExtraSmall.toPx(),
                                    center = pt
                                )
                                drawCircle(
                                    color = activeColor,
                                    radius = if (isSelected) Dimens.SpacingTiny.toPx() else Dimens.SpacingExtraSmall.toPx(),
                                    style = Stroke(width = Dimens.SpacingExtraExtraSmall.toPx()),
                                    center = pt
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMediumSmall))

            val scrollStateLabels = rememberScrollState()
            LaunchedEffect(scrollState.value) {
                scrollStateLabels.scrollTo(scrollState.value)
            }

            val labelsModifier = if (isScrollable) {
                Modifier.horizontalScroll(scrollStateLabels, enabled = false).width(itemWidth * points.size)
            } else {
                Modifier.fillMaxWidth()
            }

            Row(
                modifier = Modifier
                    .padding(start = Dimens.EvoGraphYAxisWidth)
                    .then(labelsModifier),
                verticalAlignment = Alignment.CenterVertically
            ) {
                points.forEachIndexed { index, point ->
                    val isSelected = index == selectedIndex
                    Box(
                        modifier = Modifier
                            .then(if (isScrollable) Modifier.width(itemWidth) else Modifier.weight(1f))
                            .clickable { selectedIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = point.label,
                            color = if (isSelected) activeColor else theme.secondary,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

fun getMockAnalyticsChartData(isLoad: Boolean): List<AnalyticsChartPoint> {
    return if (isLoad) {
        val months = listOf("Jan","Fev","Mar","Abr")
        val values = listOf(40f, 45f, 45f, 50f)
        months.mapIndexed { index, month ->
            AnalyticsChartPoint(month, values[index])
        }
    } else {
        val months = listOf("Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez")
        val values = listOf(1200f, 1500f, 1400f, 1800f, 2100f, 2000f, 2500f, 2400f, 2900f, 3500f, 3200f, 4000f)
        months.mapIndexed { index, month ->
            AnalyticsChartPoint(month, values[index])
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun MetricStatCardPreview() {
    EvoFitTheme {
        Row(
            modifier = Modifier.padding(Dimens.ScreenPaddingHorizontal),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            MetricStatCard(
                title = "Recorde máximo",
                value = "90kg",
                icon = Icons.Default.Star,
                modifier = Modifier.weight(1f)
            )
            MetricStatCard(
                title = "Total de séries",
                value = "147",
                icon = Icons.Default.Refresh,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun EvoExerciseChartPreview() {
    EvoFitTheme {
        Column(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            EvoExerciseChart(
                isCargaSelected = true,
                unit = MeasurementUnit.WEIGHT,
                points = getMockAnalyticsChartData(true),
                onTabChanged = {}
            )
            
            EvoExerciseChart(
                isCargaSelected = false,
                unit = MeasurementUnit.WEIGHT,
                points = getMockAnalyticsChartData(false),
                onTabChanged = {}
            )

            EvoExerciseChart(
                isCargaSelected = true,
                unit = MeasurementUnit.WEIGHT,
                points = listOf(AnalyticsChartPoint("Jan", 50f)),
                onTabChanged = {}
            )
        }
    }
}
