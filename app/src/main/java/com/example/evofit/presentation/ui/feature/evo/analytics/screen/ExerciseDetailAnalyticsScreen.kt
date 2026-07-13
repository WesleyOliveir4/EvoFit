package com.example.evofit.presentation.ui.feature.evo.analytics.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.ui.feature.evo.analytics.components.EvoExerciseChart
import com.example.evofit.presentation.ui.feature.evo.analytics.components.MetricStatCard
import com.example.evofit.presentation.ui.feature.evo.analytics.state.AnalyticsChartPoint
import com.example.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun ExerciseDetailAnalyticsScreen(
    viewModel: EvoAnalyticsViewModel,
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExerciseDetailAnalyticsContent(
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailAnalyticsContent(
    uiState: EvoAnalyticsState,
    onBackClick: () -> Unit
) {
    var isCargaSelected by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.selectedExerciseName,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Análise de evolução",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.evo_analytics_back_desc),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricStatCard(
                        title = when (uiState.unit) {
                            MeasurementUnit.WEIGHT -> "Recorde máximo"
                            MeasurementUnit.DISTANCE -> "Recorde distância"
                            MeasurementUnit.TIME -> "Recorde tempo"
                            MeasurementUnit.REPS -> "Recorde repetições"
                        },
                        value = uiState.maxRecord,
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (uiState.unit == MeasurementUnit.DISTANCE && uiState.secondaryRecord != null) {
                        MetricStatCard(
                            title = "Velocidade média",
                            value = uiState.secondaryRecord,
                            icon = Icons.Default.Info,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        MetricStatCard(
                            title = "Total de séries",
                            value = uiState.totalSets,
                            icon = Icons.Default.Refresh,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricStatCard(
                        title = "Primeiro registro",
                        value = uiState.firstRecordDate,
                        icon = Icons.Default.DateRange,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Último registro",
                        value = uiState.lastRecordDate,
                        icon = Icons.Default.Info,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            EvoExerciseChart(
                isCargaSelected = isCargaSelected,
                unit = uiState.unit,
                points = if (isCargaSelected) uiState.loadChartPoints else uiState.volumeChartPoints,
                onTabChanged = { isCargaSelected = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909, name = "Weight Analytics")
@Composable
private fun WeightAnalyticsPreview() {
    EvoFitTheme {
        ExerciseDetailAnalyticsContent(
            uiState = EvoAnalyticsState(
                selectedExerciseName = "Levantamento terra",
                unit = MeasurementUnit.WEIGHT,
                maxRecord = "120kg",
                totalSets = "45",
                firstRecordDate = "01/01/2026",
                lastRecordDate = "17/06/2026",
                loadChartPoints = listOf(
                    AnalyticsChartPoint("Jan", 80f),
                    AnalyticsChartPoint("Fev", 95f),
                    AnalyticsChartPoint("Mar", 105f),
                    AnalyticsChartPoint("Abr", 120f)
                ),
                volumeChartPoints = listOf(
                    AnalyticsChartPoint("Jan", 1200f),
                    AnalyticsChartPoint("Fev", 1500f),
                    AnalyticsChartPoint("Mar", 1800f),
                    AnalyticsChartPoint("Abr", 2200f)
                )
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909, name = "Distance Analytics")
@Composable
private fun DistanceAnalyticsPreview() {
    EvoFitTheme {
        ExerciseDetailAnalyticsContent(
            uiState = EvoAnalyticsState(
                selectedExerciseName = "Corrida",
                unit = MeasurementUnit.DISTANCE,
                maxRecord = "12.5km",
                secondaryRecord = "10.8 km/h",
                totalSets = "15",
                firstRecordDate = "10/02/2026",
                lastRecordDate = "20/06/2026",
                loadChartPoints = listOf(
                    AnalyticsChartPoint("Fev", 5f),
                    AnalyticsChartPoint("Mar", 8f),
                    AnalyticsChartPoint("Abr", 10f),
                    AnalyticsChartPoint("Mai", 12.5f)
                ),
                volumeChartPoints = listOf(
                    AnalyticsChartPoint("Fev", 9.5f),
                    AnalyticsChartPoint("Mar", 10.2f),
                    AnalyticsChartPoint("Abr", 10.5f),
                    AnalyticsChartPoint("Mai", 10.8f)
                )
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909, name = "Time Analytics")
@Composable
private fun TimeAnalyticsPreview() {
    EvoFitTheme {
        ExerciseDetailAnalyticsContent(
            uiState = EvoAnalyticsState(
                selectedExerciseName = "Prancha",
                unit = MeasurementUnit.TIME,
                maxRecord = "05:00",
                totalSets = "24",
                firstRecordDate = "15/01/2026",
                lastRecordDate = "10/06/2026",
                loadChartPoints = listOf(
                    AnalyticsChartPoint("Jan", 2f),
                    AnalyticsChartPoint("Fev", 3f),
                    AnalyticsChartPoint("Mar", 3.5f),
                    AnalyticsChartPoint("Abr", 4.5f),
                    AnalyticsChartPoint("Mai", 5f)
                )
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909, name = "Reps Analytics")
@Composable
private fun RepsAnalyticsPreview() {
    EvoFitTheme {
        ExerciseDetailAnalyticsContent(
            uiState = EvoAnalyticsState(
                selectedExerciseName = "Flexões",
                unit = MeasurementUnit.REPS,
                maxRecord = "50 reps",
                totalSets = "32",
                firstRecordDate = "01/03/2026",
                lastRecordDate = "15/06/2026",
                loadChartPoints = listOf(
                    AnalyticsChartPoint("Mar", 20f),
                    AnalyticsChartPoint("Abr", 35f),
                    AnalyticsChartPoint("Mai", 45f),
                    AnalyticsChartPoint("Jun", 50f)
                )
            ),
            onBackClick = {}
        )
    }
}
