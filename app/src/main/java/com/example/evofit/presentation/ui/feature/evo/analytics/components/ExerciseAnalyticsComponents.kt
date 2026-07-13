package com.example.evofit.presentation.ui.feature.evo.analytics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.presentation.ui.feature.evo.analytics.state.AnalyticsChartPoint
import com.example.evofit.presentation.ui.theme.EvoFitTheme

val EvoPurple = Color(0xFFA855F7)
val EvoGraphSelectedBg = Color(0xFF1C2C1E)

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
    points: List<AnalyticsChartPoint>,
    onTabChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeColor = if (isCargaSelected) Color(0xFF5ED961) else EvoPurple
    
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
            // Abas de seleção: Carga / Volume
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isCargaSelected) Color(0xFF1A271B) else Color.Transparent)
                        .clickable { onTabChanged(true) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Carga",
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
                        text = "Volume",
                        color = if (!isCargaSelected) EvoPurple else MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info de exibição do ponto selecionado (Simulando o último ponto como selecionado)
            val selectedPoint = points.lastOrNull()
            
            Text(
                text = if (selectedPoint != null) "Selecionado • ${selectedPoint.label}" else "Nenhum registro",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
            
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = selectedPoint?.value?.toInt()?.toString() ?: "0",
                    color = activeColor,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = if (isCargaSelected) "kg" else "vol",
                    color = activeColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Área de renderização do Gráfico Nativo via Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                if (points.isNotEmpty()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        
                        // Renderização das linhas de grade horizontais tracejadas
                        val gridLines = 4
                        for (i in 0 until gridLines) {
                            val y = height * (i / (gridLines - 1).toFloat())
                            drawLine(
                                color = Color(0xFF2C2C2E),
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        val maxVal = points.maxOf { it.value }.takeIf { it > 0 } ?: 1f
                        
                        // Calculate point positions
                        val drawPoints = points.mapIndexed { index, point ->
                            val x = if (points.size > 1) {
                                (index.toFloat() / (points.size - 1)) * width
                            } else {
                                width / 2
                            }
                            val y = height - (point.value / maxVal) * height * 0.8f // Keep some margin
                            Offset(x, y)
                        }

                        // Retângulo translúcido destacando o último ponto
                        if (drawPoints.isNotEmpty()) {
                            val selectedX = drawPoints.last().x
                            val barWidth = width * 0.16f
                            drawRoundRect(
                                color = if (isCargaSelected) EvoGraphSelectedBg.copy(alpha = 0.5f) else EvoPurple.copy(alpha = 0.15f),
                                topLeft = Offset(selectedX - (barWidth / 2), 0f),
                                size = androidx.compose.ui.geometry.Size(barWidth, height),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx())
                            )
                        }

                        if (drawPoints.size >= 2) {
                            // Construção do Path curvo (Bézier)
                            val strokePath = Path().apply {
                                moveTo(drawPoints.first().x, drawPoints.first().y)
                                for (i in 0 until drawPoints.size - 1) {
                                    val p1 = drawPoints[i]
                                    val p2 = drawPoints[i + 1]
                                    cubicTo(
                                        x1 = (p1.x + p2.x) / 2, y1 = p1.y,
                                        x2 = (p1.x + p2.x) / 2, y2 = p2.y,
                                        x3 = p2.x, y3 = p2.y
                                    )
                                }
                            }

                            // Preenchimento com gradiente fade-out
                            val fillPath = Path().apply {
                                addPath(strokePath)
                                lineTo(drawPoints.last().x, height)
                                lineTo(drawPoints.first().x, height)
                                close()
                            }
                            
                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(activeColor.copy(alpha = 0.3f), Color.Transparent),
                                    endY = height
                                )
                            )

                            // Desenha a linha de contorno principal
                            drawPath(
                                path = strokePath,
                                color = activeColor,
                                style = Stroke(width = 3.dp.toPx())
                            )
                        }

                        // Indicador circular no último ponto
                        if (drawPoints.isNotEmpty()) {
                            val lastPoint = drawPoints.last()
                            drawCircle(
                                color = Color(0xFF121212),
                                radius = 6.dp.toPx(),
                                center = lastPoint
                            )
                            drawCircle(
                                color = activeColor,
                                radius = 4.dp.toPx(),
                                style = Stroke(width = 2.dp.toPx()),
                                center = lastPoint
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rótulos do eixo X (Labels)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (points.size > 1) Arrangement.SpaceBetween else Arrangement.Center
            ) {
                points.forEachIndexed { index, point ->
                    val isLast = index == points.size - 1
                    Text(
                        text = point.label,
                        color = if (isLast) activeColor else MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp,
                        fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Retorna dados mockados para o gráfico de evolução.
 * @param isLoad Define se retorna dados de Carga (true) ou Volume (false)
 */
fun getMockAnalyticsChartData(isLoad: Boolean): List<AnalyticsChartPoint> {
    val months = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
    val values = if (isLoad) {
        listOf(40f, 45f, 42f, 50f, 55f, 58f, 62f, 60f, 68f, 75f, 72f, 80f)
    } else {
        listOf(1200f, 1500f, 1400f, 1800f, 2100f, 2000f, 2500f, 2400f, 2900f, 3500f, 3200f, 4000f)
    }
    
    return months.mapIndexed { index, month ->
        AnalyticsChartPoint(month, values[index])
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
                points = getMockAnalyticsChartData(true).take(6),
                onTabChanged = {}
            )
            
            // Volume state com 12 meses
            EvoExerciseChart(
                isCargaSelected = false,
                points = getMockAnalyticsChartData(false),
                onTabChanged = {}
            )

            // Estado com apenas 1 ponto (Mês centralizado)
            EvoExerciseChart(
                isCargaSelected = true,
                points = listOf(AnalyticsChartPoint("Jan", 50f)),
                onTabChanged = {}
            )
        }
    }
}
