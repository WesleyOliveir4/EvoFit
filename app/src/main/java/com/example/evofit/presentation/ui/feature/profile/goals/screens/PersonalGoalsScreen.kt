package com.example.evofit.presentation.ui.feature.profile.goals.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.commons.goals.screens.GoalWizardScreen
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.feature.profile.goals.components.GoalCard
import com.example.evofit.presentation.ui.feature.profile.goals.components.GoalFilterRow
import com.example.evofit.presentation.ui.feature.profile.goals.viewmodel.GoalUiModel
import com.example.evofit.presentation.ui.feature.profile.goals.viewmodel.PersonalGoalsViewModel
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.AppGreen
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextPrimary
import org.koin.androidx.compose.koinViewModel

@Composable
fun PersonalGoalsScreen(
    viewModel: PersonalGoalsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddGoalDialog by remember { mutableStateOf(false) }

    PersonalGoalsContent(
        goals = uiState.goals,
        onBackClick = onBackClick,
        onAddGoalClick = { showAddGoalDialog = true },
        onDeleteGoal = { viewModel.deleteGoal(it) }
    )

    if (showAddGoalDialog) {
        GoalWizardScreen(
            onBack = { showAddGoalDialog = false },
            onClose = { showAddGoalDialog = false },
            onGoalConfirmed = { goal ->
                viewModel.addGoal(goal)
                showAddGoalDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalGoalsContent(
    goals: List<GoalUiModel>,
    onBackClick: () -> Unit,
    onAddGoalClick: () -> Unit,
    onDeleteGoal: (String) -> Unit = {}
) {
    val allLabel = stringResource(id = R.string.profile_goals_filter_all)
    var selectedFilter by remember { mutableStateOf(allLabel) }

    val filteredGoals = remember(goals, selectedFilter) {
        if (selectedFilter == allLabel) goals
        else goals.filter { it.category == selectedFilter }
    }

    Scaffold(
        containerColor = AppDarkBg,
        topBar = {
            TopBarReturn(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.profile_goals_title)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddGoalClick,
                containerColor = AppGreen,
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.profile_goals_add_desc),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
        ) {
            item {
                GoalFilterRow(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (filteredGoals.isEmpty()) {
                item {
                    Text(
                        text = if (goals.isEmpty()) stringResource(id = R.string.profile_goals_empty)
                               else stringResource(id = R.string.profile_goals_filter_empty),
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            } else {
                items(filteredGoals, key = { it.id }) { goal ->
                    GoalCard(
                        title = goal.title,
                        category = goal.category,
                        currentValue = goal.currentValue,
                        targetValue = goal.targetValue,
                        percentage = goal.percentage,
                        iconRes = goal.iconRes,
                        onDeleteClick = { onDeleteGoal(goal.id) },
                        onFinishClick = { onDeleteGoal(goal.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonalGoalsScreenPreview() {
    EvoFitTheme {
        PersonalGoalsContent(
            goals = listOf(
                GoalUiModel(
                    "1",
                    "Peso alvo",
                    "Peso",
                    "82kg",
                    "75kg",
                    70,
                    R.drawable.ic_balance_2
                ),
                GoalUiModel(
                    "2",
                    "Puxada Frontal",
                    "Força",
                    "0.0",
                    "90.0",
                    0,
                    R.drawable.ic_back
                ),
                GoalUiModel(
                    "3",
                    "Corrida 5km",
                    "Cardio",
                    "4.5km",
                    "10.0km",
                    45,
                    R.drawable.ic_cardio
                )
            ),
            onBackClick = {},
            onAddGoalClick = {}
        )
    }
}
