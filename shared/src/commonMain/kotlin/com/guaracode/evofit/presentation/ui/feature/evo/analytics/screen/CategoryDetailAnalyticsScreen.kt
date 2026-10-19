package com.guaracode.evofit.presentation.ui.feature.evo.analytics.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.components.EvoExerciseChart
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.components.MetricStatCard
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTracker
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.analytics_detail_avg_speed
import evofit.shared.generated.resources.analytics_detail_first_record
import evofit.shared.generated.resources.analytics_detail_last_record
import evofit.shared.generated.resources.analytics_detail_record_distance
import evofit.shared.generated.resources.analytics_detail_record_max
import evofit.shared.generated.resources.analytics_detail_record_reps
import evofit.shared.generated.resources.analytics_detail_record_time
import evofit.shared.generated.resources.analytics_detail_subtitle
import evofit.shared.generated.resources.analytics_detail_total_sets
import evofit.shared.generated.resources.ic_balance
import evofit.shared.generated.resources.ic_speed
import evofit.shared.generated.resources.ic_today
import evofit.shared.generated.resources.ic_trophy
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun CategoryDetailAnalyticsScreen(
    viewModel: EvoAnalyticsViewModel,
    tracker: EvoAnalyticsTracker = koinInject(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

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
                subtitle = stringResource(Res.string.analytics_detail_subtitle),
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
                            MeasurementUnit.WEIGHT -> stringResource(Res.string.analytics_detail_record_max)
                            MeasurementUnit.DISTANCE -> stringResource(Res.string.analytics_detail_record_distance)
                            MeasurementUnit.TIME -> stringResource(Res.string.analytics_detail_record_time)
                            MeasurementUnit.REPS -> stringResource(Res.string.analytics_detail_record_reps)
                        },
                        value = uiState.maxRecord,
                        icon = Icons.Default.Refresh,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (uiState.isWeightAnalysis) {
                        MetricStatCard(
                            title = "Peso Mínimo",
                            value = uiState.secondaryRecord ?: "-",
                            icon = Icons.Default.Refresh,
                            modifier = Modifier.weight(1f)
                        )
                    } else if (uiState.unit == MeasurementUnit.DISTANCE && uiState.secondaryRecord != null) {
                        MetricStatCard(
                            title = stringResource(Res.string.analytics_detail_avg_speed),
                            value = uiState.secondaryRecord,
                            icon = Icons.Default.Refresh,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        MetricStatCard(
                            title = stringResource(Res.string.analytics_detail_total_sets),
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
                        title = stringResource(Res.string.analytics_detail_first_record),
                        value = uiState.firstRecordDate,
                        icon = Icons.Default.DateRange,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = stringResource(Res.string.analytics_detail_last_record),
                        value = uiState.lastRecordDate,
                        icon = Icons.Default.DateRange,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (uiState.isWeightAnalysis) {
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

@Preview
@Composable
private fun CategoryDetailAnalyticsContentPreview() {
    EvoFitTheme {
        CategoryDetailAnalyticsContent(
            uiState = EvoAnalyticsState(
                selectedExerciseName = "Supino Reto",
                maxRecord = "100kg"
            ),
            onBackClick = {}
        )
    }
}
