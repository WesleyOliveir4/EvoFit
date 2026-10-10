package com.guaracode.evofit.presentation.ui.feature.evo.analytics.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guaracode.evofit.R
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.components.EvoExerciseChart
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.components.MetricStatCard
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.state.AnalyticsChartPoint
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTracker
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.compose.koinInject

@Composable
fun CategoryDetailAnalyticsScreen(
    viewModel: EvoAnalyticsViewModel,
    tracker: EvoAnalyticsTracker = koinInject(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.selectedExerciseName) {
        if (uiState.selectedExerciseName.isNotEmpty()) {
            tracker.trackCategoryDetailAnalyticsScreenView(uiState.selectedExerciseName)
        }
    }

    CategoryDetailAnalyticsContent(
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailAnalyticsContent(
    uiState: EvoAnalyticsState,
    onBackClick: () -> Unit
) {
    var isCargaSelected by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarReturn(
                title = uiState.selectedExerciseName,
                subtitle = stringResource(R.string.analytics_detail_subtitle),
                onBackClick = onBackClick,
                isCenterAligned = false
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
                ) {
                    MetricStatCard(
                        title = if (uiState.isWeightAnalysis) "Peso Máximo" else when (uiState.unit) {
                            MeasurementUnit.WEIGHT -> stringResource(R.string.analytics_detail_record_max)
                            MeasurementUnit.DISTANCE -> stringResource(R.string.analytics_detail_record_distance)
                            MeasurementUnit.TIME -> stringResource(R.string.analytics_detail_record_time)
                            MeasurementUnit.REPS -> stringResource(R.string.analytics_detail_record_reps)
                        },
                        value = uiState.maxRecord,
                        icon = ImageVector.vectorResource(R.drawable.ic_trophy),
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (uiState.isWeightAnalysis) {
                        MetricStatCard(
                            title = "Peso Mínimo",
                            value = uiState.secondaryRecord ?: "-",
                            icon = ImageVector.vectorResource(R.drawable.ic_balance),
                            modifier = Modifier.weight(1f)
                        )
                    } else if (uiState.unit == MeasurementUnit.DISTANCE && uiState.secondaryRecord != null) {
                        MetricStatCard(
                            title = stringResource(R.string.analytics_detail_avg_speed),
                            value = uiState.secondaryRecord.orEmpty(),
                            icon = ImageVector.vectorResource(R.drawable.ic_speed),
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        MetricStatCard(
                            title = stringResource(R.string.analytics_detail_total_sets),
                            value = uiState.totalSets,
                            icon = Icons.Default.Refresh,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
                ) {
                    MetricStatCard(
                        title = stringResource(R.string.analytics_detail_first_record),
                        value = uiState.firstRecordDate,
                        icon = Icons.Default.DateRange,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = stringResource(R.string.analytics_detail_last_record),
                        value = uiState.lastRecordDate,
                        icon = ImageVector.vectorResource(R.drawable.ic_today),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (uiState.isWeightAnalysis) {
                // Para peso, mostramos apenas o gráfico sem abas
                EvoExerciseChart(
                    isCargaSelected = true,
                    unit = MeasurementUnit.WEIGHT,
                    points = uiState.loadChartPoints,
                    onTabChanged = {},
                    showTabs = false
                )
            } else {
                EvoExerciseChart(
                    isCargaSelected = isCargaSelected,
                    unit = uiState.unit,
                    points = if (isCargaSelected) uiState.loadChartPoints else uiState.volumeChartPoints,
                    onTabChanged = { isCargaSelected = it }
                )
            }
            
            Spacer(modifier = Modifier.height(Dimens.ScreenPaddingHorizontal))
        }
    }
}
