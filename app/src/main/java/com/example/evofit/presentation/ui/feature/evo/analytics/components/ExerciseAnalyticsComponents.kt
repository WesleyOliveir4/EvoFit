package com.example.evofit.presentation.ui.feature.evo.analytics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.ui.feature.evo.analytics.state.AnalyticsChartPoint
import com.example.evofit.presentation.ui.theme.EvoFitTheme

import androidx.compose.ui.platform.LocalConfiguration
import java.util.Locale

val EvoPurple = Color(0xFFA855F7)
val EvoGraphSelectedBg = Color(0xFF1C2C1E)

private data class YAxisConfig(val min: Float, val max: Float, val step: Float)

private fun calculateYAxisConfig(points: List<AnalyticsChartPoint>): YAxisConfig {
    val maxVal = points.maxOfOrNull { it.value } ?: 50f
    // Garante um respiro no topo de 25%
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
    iconColor: Color = Color(0xFF5ED961),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(115.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = value,
                    color = iconColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
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
    val activeColor = if (isCargaSelected) Color(0xFF5ED961) else EvoPurple
    
    val locale = LocalConfiguration.current.locales[0]

    // Estado interno para rastrear qual ponto está selecionado (padrão é o último)
    var selectedIndex by androidx.compose.runtime.remember(points) {
        androidx.compose.runtime.mutableStateOf(if (points.isNotEmpty()) points.size - 1 else -1)
    }

    val showTabs = unit == MeasurementUnit.WEIGHT || unit == MeasurementUnit.DISTANCE

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            if (showTabs) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val primaryTabLabel = when (unit) {
                        MeasurementUnit.WEIGHT -> "Carga"
                        MeasurementUnit.DISTANCE -> "Distância"
                        else -> ""
                    }
                    val secondaryTabLabel = when (unit) {
                        MeasurementUnit.WEIGHT -> "Volume"
                        MeasurementUnit.DISTANCE -> "Velocidade"
                        else -> ""
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isCargaSelected) Color(0xFF1A271B) else Color.Transparent)
                            .clickable { onTabChanged(true) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = primaryTabLabel,
                            color = if (isCargaSelected) Color(0xFF5ED961) else MaterialTheme.colorScheme.secondary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (!isCargaSelected) Color(0xFF261A35) else Color.Transparent)
                            .clickable { onTabChanged(false) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = secondaryTabLabel,
                            color = if (!isCargaSelected) EvoPurple else MaterialTheme.colorScheme.secondary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            val selectedPoint = if (selectedIndex in points.indices) points[selectedIndex] else null
            
            Text(
                text = if (selectedPoint != null) "Média • ${selectedPoint.label}" else "Nenhum registro",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
            
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val formattedValue = when (unit) {
                    MeasurementUnit.TIME -> {
                        val totalMinutes = selectedPoint?.value ?: 0f
                        val hours = (totalMinutes / 60).toInt()
                        val minutes = (totalMinutes % 60).toInt()
                        if (hours > 0) String.format(locale, "%dh %02dm", hours, minutes)
                        else String.format(locale, "%dm", minutes)
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
                    color = activeColor,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black
                )

                val unitLabel = when (unit) {
                    MeasurementUnit.WEIGHT -> if (isCargaSelected) "kg" else "vol"
                    MeasurementUnit.DISTANCE -> if (isCargaSelected) "km" else "km/h"
                    MeasurementUnit.TIME -> "" // Já formatado no valor
                    MeasurementUnit.REPS -> "reps"
                }

                if (unitLabel.isNotEmpty()) {
                    Text(
                        text = unitLabel,
                        color = activeColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Área do gráfico (Rule 1.1 e 1.3)
            val scrollState = androidx.compose.foundation.rememberScrollState()
            val itemWidth = 72.dp 
            val labelColor = MaterialTheme.colorScheme.secondary.toArgb()
            val isScrollable = points.size > 5
            
            val yAxisConfig = calculateYAxisConfig(points)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // Legendas Y Fixas (Rule 1.1)
                Canvas(modifier = Modifier
                    .fillMaxHeight()
                    .width(44.dp)
                    .padding(vertical = 20.dp)
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
                            size.width - 6.dp.toPx(),
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
                            .padding(vertical = 20.dp)
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
                                    color = Color(0xFF2C2C2E),
                                    start = Offset(0f, y),
                                    end = Offset(canvasWidth, y),
                                    strokeWidth = 0.5.dp.toPx()
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
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx())
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
                                    style = Stroke(width = 3.dp.toPx())
                                )
                            }

                            drawPoints.forEachIndexed { index, pt ->
                                val isSelected = index == selectedIndex
                                drawCircle(
                                    color = if (isSelected) Color.White else Color(0xFF121212),
                                    radius = if (isSelected) 6.dp.toPx() else 4.dp.toPx(),
                                    center = pt
                                )
                                drawCircle(
                                    color = activeColor,
                                    radius = if (isSelected) 6.dp.toPx() else 4.dp.toPx(),
                                    style = Stroke(width = 2.dp.toPx()),
                                    center = pt
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val scrollStateLabels = androidx.compose.foundation.rememberScrollState()
            androidx.compose.runtime.LaunchedEffect(scrollState.value) {
                scrollStateLabels.scrollTo(scrollState.value)
            }

            val labelsModifier = if (isScrollable) {
                Modifier.horizontalScroll(scrollStateLabels, enabled = false).width(itemWidth * points.size)
            } else {
                Modifier.fillMaxWidth()
            }

            Row(
                modifier = Modifier
                    .padding(start = 44.dp)
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
                            color = if (isSelected) activeColor else MaterialTheme.colorScheme.secondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
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
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Carga state com 6 meses
            EvoExerciseChart(
                isCargaSelected = true,
                unit = MeasurementUnit.WEIGHT,
                points = getMockAnalyticsChartData(true),
                onTabChanged = {}
            )
            
            // Volume state com 12 meses
            EvoExerciseChart(
                isCargaSelected = false,
                unit = MeasurementUnit.WEIGHT,
                points = getMockAnalyticsChartData(false),
                onTabChanged = {}
            )

            // Estado com apenas 1 ponto (Mês centralizado)
            EvoExerciseChart(
                isCargaSelected = true,
                unit = MeasurementUnit.WEIGHT,
                points = listOf(AnalyticsChartPoint("Jan", 50f)),
                onTabChanged = {}
            )
        }
    }
}
