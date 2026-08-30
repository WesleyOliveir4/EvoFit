package com.example.evofit.presentation.ui.feature.evo.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.domain.model.EvoPeriod
import com.example.evofit.navigation.NavRoutes
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.components.EvoFitDropdownFilter
import com.example.evofit.presentation.ui.feature.evo.home.components.*
import com.example.evofit.presentation.ui.feature.evo.home.state.EvoHomeUiState
import com.example.evofit.presentation.ui.feature.evo.home.viewmodel.EvoHomeViewModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun EvoHomeScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: EvoHomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    EvoHomeContent(
        uiState = uiState,
        onPeriodSelected = { viewModel.onPeriodSelected(it) },
        onNavigate = onNavigate
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
                currentRoute = NavRoutes.Evo.route,
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
            val periodNames = periods.map { stringResource(it.displayNameRes) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.SpacingMedium, bottom = Dimens.SpacingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.evo_home_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayLarge
                )

                EvoFitDropdownFilter(
                    selectedOption = stringResource(uiState.selectedPeriod.displayNameRes),
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
                        text = stringResource(R.string.evo_home_insufficient_data),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = Dimens.SpacingSmall)
                    )
                } else {
                    gains.forEachIndexed { index, gain ->
                        StrengthProgressRow(
                            position = stringResource(R.string.evo_home_ranking_format, index + 1),
                            exerciseName = gain.exerciseName,
                            progressValue = stringResource(R.string.evo_home_gain_kg_format, gain.gainKg)
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
                    muscleName = evolution?.muscleGroupName ?: stringResource(R.string.evo_home_empty_value),
                    percentage = evolution?.let { 
                        stringResource(R.string.evo_home_evolution_percentage_format, it.evolutionPercentage) 
                    } ?: stringResource(R.string.evo_home_empty_value),
                    modifier = Modifier.weight(1.4f)
                )

                WorkoutsCompletedCard(
                    count = uiState.workoutsCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            LeastTrainedCard(
                muscleName = uiState.leastTrainedGroup?.first ?: stringResource(R.string.evo_home_empty_value),
                sessionsCount = uiState.leastTrainedGroup?.second ?: 0
            )

            KmPerWeekCard(
                kmPerWeek = uiState.kmPerWeek
            )

            AverageWorkoutTimeCard(
                averageTimeMinutes = uiState.averageWorkoutTime
            )

            Text(
                text = stringResource(R.string.evo_home_exercise_analysis_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = Dimens.SpacingSmall)
            )

            ExerciseAnalyticsCard(
                onClick = { onNavigate(NavRoutes.MuscleGroupSelection.route) }
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
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
