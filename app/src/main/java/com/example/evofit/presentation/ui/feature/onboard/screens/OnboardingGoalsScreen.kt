package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.data.model.ExerciseModel
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.presentation.mapper.getDisplayText
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.evofit.presentation.ui.feature.onboard.components.ActiveGoalItem
import com.example.evofit.presentation.ui.feature.onboard.components.AddNewGoalButton
import com.example.evofit.presentation.ui.feature.onboard.components.GoalTag
import com.example.evofit.presentation.ui.feature.onboard.components.NewGoalDialog
import com.example.evofit.presentation.ui.feature.onboard.components.OnboardingButton
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingGoalsScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val userData by viewModel.userData.collectAsStateWithLifecycle()

    OnboardingGoalsContent(
        activeGoals = userData.goals,
        suggestions = remember { viewModel.getSuggestions() },
        muscleGroups = remember { viewModel.getMuscleGroups() },
        getExercises = remember { { viewModel.getExercisesByGroup(it) } },
        currentPage = currentPage,
        totalPages = totalPages,
        onAddGoal = remember { { goal -> viewModel.addGoal(goal) } },
        onRemoveGoal = remember { { goal -> viewModel.removeGoal(goal) } },
        onSkip = remember { { viewModel.saveAndNext(onSkip) } },
        onFinish = remember { { viewModel.saveAndNext(onContinue) } }
    )
}

@Composable
fun OnboardingGoalsContent(
    activeGoals: List<UserGoal>,
    suggestions: List<GoalSuggestion>,
    muscleGroups: List<MuscleGroupModel>,
    getExercises: (String) -> List<ExerciseModel>,
    currentPage: Int,
    totalPages: Int,
    onAddGoal: (UserGoal) -> Unit,
    onRemoveGoal: (UserGoal) -> Unit,
    onSkip: () -> Unit,
    onFinish: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedSuggestion by remember { mutableStateOf<GoalSuggestion?>(null) }

    if (showDialog) {
        NewGoalDialog(
            onDismissRequest = {
                showDialog = false
                selectedSuggestion = null
            },
            onGoalConfirmed = { newGoal ->
                onAddGoal(newGoal)
            },
            muscleGroups = muscleGroups,
            getExercises = getExercises,
            initialSuggestion = selectedSuggestion
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.onboarding_goals_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.onboarding_goals_description),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { suggestion ->
                GoalTag(
                    text = suggestion.text,
                    onClick = {
                        selectedSuggestion = suggestion
                        showDialog = true
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val context = LocalContext.current
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = activeGoals,
                key = { it.id }
            ) { goal ->
                ActiveGoalItem(
                    text = goal.getDisplayText(context),
                    onRemoveClick = {
                        onRemoveGoal(goal)
                    }
                )
            }
            
            item {
                AddNewGoalButton(
                    onClick = { showDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onSkip) {
            Text(
                text = stringResource(R.string.onboarding_goals_button_skip),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        PageIndicators(
            pageCount = totalPages,
            selectedPage = currentPage,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OnboardingButton(
            text = stringResource(R.string.onboarding_button_continue),
            enabled = activeGoals.isNotEmpty(),
            onClick = onFinish
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        maxItemsInEachRow = maxItemsInEachRow
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingGoalsScreenPreview() {
    EvoFitTheme {
        OnboardingGoalsContent(
            activeGoals = emptyList(),
            suggestions = emptyList(),
            muscleGroups = emptyList(),
            getExercises = { emptyList() },
            currentPage = 2,
            totalPages = 4,
            onAddGoal = {},
            onRemoveGoal = {},
            onSkip = {},
            onFinish = {}
        )
    }
}
