package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.presentation.model.GoalUIModel
import com.example.evofit.presentation.ui.feature.commons.goals.screens.GoalWizardScreen
import com.example.evofit.presentation.ui.feature.components.EvoFitButton
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.feature.onboard.components.ActiveGoalItem
import com.example.evofit.presentation.ui.feature.onboard.components.AddNewGoalButton
import com.example.evofit.presentation.ui.feature.onboard.components.GoalTag
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.theme.Dimens
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
        GoalWizardScreen(
            onBack = {
                showDialog = false
                selectedSuggestion = null
            },
            onClose = {
                showDialog = false
                selectedSuggestion = null
            },
            onGoalConfirmed = { newGoal ->
                onAddGoal(newGoal)
                showDialog = false
                selectedSuggestion = null
            },
            initialSuggestion = selectedSuggestion
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        topBar = {
            TopBarReturn(
                onBackClick = onBack
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingExtraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(onClick = onSkip) {
                    Text(
                        text = stringResource(R.string.onboarding_goals_button_skip),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                PageIndicators(
                    pageCount = totalPages,
                    selectedPage = currentPage,
                    modifier = Modifier.padding(bottom = Dimens.SpacingMedium)
                )

                EvoFitButton(
                    text = stringResource(R.string.onboarding_button_continue),
                    enabled = activeGoals.isNotEmpty(),
                    onClick = onFinish
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.onboarding_goals_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                Text(
                    text = stringResource(R.string.onboarding_goals_description),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
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

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
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

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
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
