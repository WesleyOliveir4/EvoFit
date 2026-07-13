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
import com.example.evofit.presentation.ui.feature.evo.analytics.components.EvoExerciseChart
import com.example.evofit.presentation.ui.feature.evo.analytics.components.MetricStatCard
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
                        title = "Recorde máximo",
                        value = uiState.maxRecord,
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Total de séries",
                        value = uiState.totalSets,
                        icon = Icons.Default.Refresh,
                        modifier = Modifier.weight(1f)
                    )
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
                points = if (isCargaSelected) uiState.loadChartPoints else uiState.volumeChartPoints,
                onTabChanged = { isCargaSelected = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseDetailAnalyticsScreenPreview() {
    EvoFitTheme {
        ExerciseDetailAnalyticsContent(
            uiState = EvoAnalyticsState(
                selectedExerciseName = "Levantamento terra",
                maxRecord = "90kg",
                totalSets = "147",
                firstRecordDate = "13/01/2026",
                lastRecordDate = "17/06/2026"
            ),
            onBackClick = {}
        )
    }
}
