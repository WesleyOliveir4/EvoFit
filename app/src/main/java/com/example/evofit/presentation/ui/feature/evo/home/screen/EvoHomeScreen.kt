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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.navigation.NavRoutes
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.components.EvoFitDropdownFilter
import com.example.evofit.presentation.ui.feature.evo.home.components.MostEvolvedCard
import com.example.evofit.presentation.ui.feature.evo.home.components.StrengthGainsCard
import com.example.evofit.presentation.ui.feature.evo.home.components.StrengthProgressRow
import com.example.evofit.presentation.ui.feature.evo.home.components.WorkoutsCompletedCard
import com.example.evofit.presentation.ui.theme.EvoFitTheme

import androidx.compose.runtime.collectAsState
import com.example.evofit.domain.model.EvoPeriod
import com.example.evofit.presentation.ui.feature.evo.home.state.EvoHomeUiState
import com.example.evofit.presentation.ui.feature.evo.home.viewmodel.EvoHomeViewModel
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
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .windowInsetsPadding(WindowInsets.statusBars)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Título da Aba
                Text(
                    text = stringResource(R.string.evo_home_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )

                // Filtro de Período funcional
                EvoFitDropdownFilter(
                    selectedOption = uiState.selectedPeriod.displayName,
                    options = EvoPeriod.entries.map { it.displayName },
                    onOptionSelected = { displayName ->
                        EvoPeriod.entries.find { it.displayName == displayName }?.let {
                            onPeriodSelected(it)
                        }
                    }
                )
            }
        },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            StrengthGainsCard {
                val gains = uiState.strengthGains
                if (gains.isNullOrEmpty()) {
                    Text(
                        text = "Dados insuficientes para este período",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    gains.forEachIndexed { index, gain ->
                        StrengthProgressRow(
                            position = "${index + 1}º",
                            exerciseName = gain.exerciseName,
                            progressValue = "+${String.format("%.1f", gain.gainKg)}kg"
                        )
                        if (index < gains.size - 1) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val evolution = uiState.mostEvolvedMuscle
                MostEvolvedCard(
                    muscleName = evolution?.muscleGroupName ?: "---",
                    percentage = evolution?.let { "+${String.format("%.0f", it.evolutionPercentage)}%" } ?: "---",
                    modifier = Modifier.weight(1f)
                )

                WorkoutsCompletedCard(
                    count = uiState.workoutsCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EvoHomeContentPreview() {
    EvoFitTheme() {
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
