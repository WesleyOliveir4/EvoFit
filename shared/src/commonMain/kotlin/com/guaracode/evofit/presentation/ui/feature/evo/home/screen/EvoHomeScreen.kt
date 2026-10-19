package com.guaracode.evofit.presentation.ui.feature.evo.home.screen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.guaracode.evofit.domain.model.EvoPeriod
import com.guaracode.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitDropdownFilter
import com.guaracode.evofit.presentation.ui.feature.evo.home.components.AverageWorkoutTimeCard
import com.guaracode.evofit.presentation.ui.feature.evo.home.components.ExerciseAnalyticsCard
import com.guaracode.evofit.presentation.ui.feature.evo.home.components.KmPerWeekCard
import com.guaracode.evofit.presentation.ui.feature.evo.home.components.LeastTrainedCard
import com.guaracode.evofit.presentation.ui.feature.evo.home.components.MostEvolvedCard
import com.guaracode.evofit.presentation.ui.feature.evo.home.components.StrengthGainsCard
import com.guaracode.evofit.presentation.ui.feature.evo.home.components.StrengthProgressRow
import com.guaracode.evofit.presentation.ui.feature.evo.home.components.WorkoutsCompletedCard
import com.guaracode.evofit.presentation.ui.feature.evo.home.state.EvoHomeUiState
import com.guaracode.evofit.presentation.ui.feature.evo.home.tracking.EvoHomeTracker
import com.guaracode.evofit.presentation.ui.feature.evo.home.viewmodel.EvoHomeViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.evo_home_empty_value
import evofit.shared.generated.resources.evo_home_evolution_percentage_format
import evofit.shared.generated.resources.evo_home_exercise_analysis_title
import evofit.shared.generated.resources.evo_home_gain_kg_format
import evofit.shared.generated.resources.evo_home_insufficient_data
import evofit.shared.generated.resources.evo_home_ranking_format
import evofit.shared.generated.resources.evo_home_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private fun EvoPeriod.getDisplayName(): String {
    return when (this) {
        EvoPeriod.LAST_30_DAYS -> "30 dias"
        EvoPeriod.LAST_90_DAYS -> "3 meses"
        EvoPeriod.LAST_180_DAYS -> "6 meses"
        EvoPeriod.ALL_TIME -> "Todo o período"
    }
}

@Composable
fun EvoHomeScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: EvoHomeViewModel = koinViewModel(),
    tracker: EvoHomeTracker = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        tracker.trackEvoHomeScreenView()
    }

    EvoHomeContent(
        uiState = uiState,
        onPeriodSelected = { viewModel.onPeriodSelected(it) },
        onNavigate = { route ->
            if (route == "muscle_group_selection") {
                tracker.trackExerciseAnalyticsCardClicked()
            }
            onNavigate(route)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvoHomeContent(
    uiState: EvoHomeUiState,
    onPeriodSelected: (EvoPeriod) -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomNavigation(
                currentRoute = "evo",
                onNavigate = onNavigate
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
            val periods = EvoPeriod.entries
            val periodNames = periods.map { it.getDisplayName() }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.SpacingMedium, bottom = Dimens.SpacingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.evo_home_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayLarge
                )

                EvoFitDropdownFilter(
                    selectedOption = uiState.selectedPeriod.getDisplayName(),
                    options = periodNames,
                    onOptionSelected = { selectedName ->
                        val index = periodNames.indexOf(selectedName)
                        if (index != -1) {
                            onPeriodSelected(periods[index])
                        }
                    }
                )
            }
            
            StrengthGainsCard {
                val gains = uiState.strengthGains
                if (gains.isNullOrEmpty()) {
                    Text(
                        text = stringResource(Res.string.evo_home_insufficient_data),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = Dimens.SpacingSmall)
                    )
                } else {
                    gains.forEachIndexed { index, gain ->
                        StrengthProgressRow(
                            position = stringResource(Res.string.evo_home_ranking_format, index + 1),
                            exerciseName = gain.exerciseName,
                            progressValue = stringResource(Res.string.evo_home_gain_kg_format, gain.gainKg)
                        )
                        if (index < gains.size - 1) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                thickness = Dimens.BorderWidthThin / 2,
                                modifier = Modifier.padding(vertical = Dimens.SpacingMediumSmall)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
            ) {
                val evolution = uiState.mostEvolvedMuscle
                MostEvolvedCard(
                    muscleName = evolution?.muscleGroupName ?: stringResource(Res.string.evo_home_empty_value),
                    percentage = evolution?.let { 
                        stringResource(Res.string.evo_home_evolution_percentage_format, it.evolutionPercentage) 
                    } ?: stringResource(Res.string.evo_home_empty_value),
                    modifier = Modifier.weight(1.4f)
                )

                WorkoutsCompletedCard(
                    count = uiState.workoutsCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
            ) {
                KmPerWeekCard(
                    kmPerWeek = uiState.kmPerWeek,
                    modifier = Modifier.weight(1f)
                )
                LeastTrainedCard(
                    muscleName = uiState.leastTrainedGroup?.first ?: stringResource(Res.string.evo_home_empty_value),
                    sessionsCount = uiState.leastTrainedGroup?.second ?: 0,
                    modifier = Modifier.weight(1.4f)
                )
            }

            AverageWorkoutTimeCard(
                averageTimeMinutes = uiState.averageWorkoutTime
            )

            Text(
                text = stringResource(Res.string.evo_home_exercise_analysis_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = Dimens.SpacingSmall)
            )

            ExerciseAnalyticsCard(
                onClick = { onNavigate("muscle_group_selection") }
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        }
    }
}

@Preview
@Composable
private fun EvoHomeContentPreview() {
    EvoFitTheme {
        EvoHomeContent(
            uiState = EvoHomeUiState(
                selectedPeriod = EvoPeriod.LAST_90_DAYS,
                workoutsCount = 24
            ),
            onPeriodSelected = {},
            onNavigate = {}
        )
    }
}
