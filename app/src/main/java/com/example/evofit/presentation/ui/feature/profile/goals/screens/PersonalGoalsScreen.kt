package com.example.evofit.presentation.ui.feature.profile.goals.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.presentation.ui.feature.onboard.components.NewGoalDialog
import com.example.evofit.presentation.ui.feature.profile.goals.components.GoalCard
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
        NewGoalDialog(
            onDismissRequest = { showAddGoalDialog = false },
            onGoalConfirmed = { goal ->
                viewModel.addGoal(goal)
                showAddGoalDialog = false
            },
            muscleGroups = viewModel.getMuscleGroups(),
            getExercises = { viewModel.getExercisesByGroup(it) }
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
    Scaffold(
        containerColor = AppDarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile_goals_title),
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.new_workout_back_desc),
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppDarkBg)
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
                Row(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.profile_goals_subtitle),
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (goals.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.profile_goals_empty),
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            } else {
                items(goals, key = { it.id }) { goal ->
                    GoalCard(
                        title = goal.title,
                        category = goal.category,
                        currentValue = goal.currentValue,
                        targetValue = goal.targetValue,
                        percentage = goal.percentage,
                        onDeleteClick = { onDeleteGoal(goal.id) }
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
                    90, 
                    UserGoal.Weight("1", "75")
                ),
                GoalUiModel(
                    "2", 
                    "Supino reto 100kg", 
                    "Força", 
                    "90kg", 
                    "100kg", 
                    90, 
                    UserGoal.Strength("2", "Supino Reto", "100", MeasurementUnit.WEIGHT)
                )
            ),
            onBackClick = {},
            onAddGoalClick = {}
        )
    }
}
