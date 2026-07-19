package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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
import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.presentation.model.GoalUIModel
import com.example.evofit.presentation.ui.feature.components.EvoFitButton
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.feature.onboard.components.ActiveGoalItem
import com.example.evofit.presentation.ui.feature.onboard.components.AddNewGoalButton
import com.example.evofit.presentation.ui.feature.onboard.components.GoalTag
import com.example.evofit.presentation.ui.feature.onboard.components.NewGoalDialog
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingGoalsScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingGoalsContent(
        activeGoals = uiState.goals,
        suggestions = remember { viewModel.getSuggestions() },
        muscleGroups = remember { viewModel.getMuscleGroups() },
        getExercises = remember { { viewModel.getExercisesByGroup(it) } },
        currentPage = currentPage,
        totalPages = totalPages,
        onAddGoal = remember { { goal -> viewModel.addGoal(goal) } },
        onRemoveGoal = remember { { goalId -> viewModel.removeGoal(goalId) } },
        onSkip = remember { { viewModel.saveAndNext(onSkip) } },
        onFinish = remember { { viewModel.saveAndNext(onContinue) } },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingGoalsContent(
    activeGoals: List<GoalUIModel>,
    suggestions: List<GoalSuggestion>,
    muscleGroups: List<MuscleGroup>,
    getExercises: (String) -> List<Exercise>,
    currentPage: Int,
    totalPages: Int,
    onAddGoal: (UserGoal) -> Unit,
    onRemoveGoal: (String) -> Unit,
    onSkip: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.onboarding_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
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
                    text = goal.displayText,
                    onRemoveClick = {
                        onRemoveGoal(goal.id)
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

            EvoFitButton(
            text = stringResource(R.string.onboarding_button_continue),
            enabled = activeGoals.isNotEmpty(),
            onClick = onFinish
        )

        Spacer(modifier = Modifier.height(32.dp))
        }
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
            currentPage = 4,
            totalPages = 6,
            onAddGoal = {},
            onRemoveGoal = {},
            onSkip = {},
            onFinish = {},
            onBack = {}
        )
    }
}
