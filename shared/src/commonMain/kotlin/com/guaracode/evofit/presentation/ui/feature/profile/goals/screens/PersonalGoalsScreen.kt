package com.guaracode.evofit.presentation.ui.feature.profile.goals.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.feature.commons.goals.screens.GoalWizardScreen
import com.guaracode.evofit.presentation.ui.feature.profile.goals.components.GoalCard
import com.guaracode.evofit.presentation.ui.feature.profile.goals.components.GoalFilterRow
import com.guaracode.evofit.presentation.ui.feature.profile.goals.tracking.PersonalGoalsTracker
import com.guaracode.evofit.presentation.ui.feature.profile.goals.viewmodel.GoalUiModel
import com.guaracode.evofit.presentation.ui.feature.profile.goals.viewmodel.PersonalGoalsViewModel
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import com.guaracode.evofit.presentation.ui.theme.evoColors
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_balance_2
import evofit.shared.generated.resources.new_workout_back_desc
import evofit.shared.generated.resources.profile_goals_add_desc
import evofit.shared.generated.resources.profile_goals_empty
import evofit.shared.generated.resources.profile_goals_filter_all
import evofit.shared.generated.resources.profile_goals_filter_empty
import evofit.shared.generated.resources.profile_goals_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonalGoalsScreen(
    viewModel: PersonalGoalsViewModel = koinViewModel(),
    tracker: PersonalGoalsTracker = koinInject(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddGoalDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tracker.trackPersonalGoalsScreenView()
    }

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
    val allLabel = stringResource(Res.string.profile_goals_filter_all)
    var selectedFilter by remember { mutableStateOf(allLabel) }

    val filteredGoals = remember(goals, selectedFilter) {
        if (selectedFilter == allLabel) goals
        else goals.filter { it.category == selectedFilter }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.profile_goals_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.new_workout_back_desc),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddGoalClick,
                containerColor = MaterialTheme.evoColors.green,
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.profile_goals_add_desc),
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
                        text = if (goals.isEmpty()) stringResource(Res.string.profile_goals_empty)
                               else stringResource(Res.string.profile_goals_filter_empty),
                        color = MaterialTheme.colorScheme.onBackground,
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

@Preview
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
                    Res.drawable.ic_balance_2
                )
            ),
            onBackClick = {},
            onAddGoalClick = {}
        )
    }
}
