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

@Composable
fun EvoHomeScreen(
    onNavigate: (String) -> Unit = {}
) {
    var selectedPeriod by remember { mutableStateOf("3 meses") }

    EvoHomeContent(
        selectedPeriod = selectedPeriod,
        onPeriodSelected = { selectedPeriod = it },
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvoHomeContent(
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit,
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
                    selectedOption = selectedPeriod,
                    onOptionSelected = onPeriodSelected
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
                StrengthProgressRow(position = stringResource(R.string.evo_home_top_1), exerciseName = stringResource(R.string.evo_home_mock_exercise_1), progressValue = stringResource(R.string.evo_home_mock_progress_1))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 12.dp))
                
                StrengthProgressRow(position = stringResource(R.string.evo_home_top_2), exerciseName = stringResource(R.string.evo_home_mock_exercise_2), progressValue = stringResource(R.string.evo_home_mock_progress_2))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 12.dp))
                
                StrengthProgressRow(position = stringResource(R.string.evo_home_top_3), exerciseName = stringResource(R.string.evo_home_mock_exercise_3), progressValue = stringResource(R.string.evo_home_mock_progress_3))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MostEvolvedCard(
                    muscleName = stringResource(R.string.evo_home_mock_muscle_evolved),
                    percentage = stringResource(R.string.evo_home_mock_muscle_percentage),
                    modifier = Modifier.weight(1f)
                )

                WorkoutsCompletedCard(
                    count = stringResource(R.string.evo_home_mock_workouts_count),
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
            selectedPeriod = "3 meses",
            onPeriodSelected = {},
            onNavigate = {}
        )
    }
}
