package com.guaracode.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guaracode.evofit.domain.model.GoalSuggestion
import com.guaracode.evofit.domain.model.UserGoal
import com.guaracode.evofit.presentation.model.GoalUIModel
import com.guaracode.evofit.presentation.ui.feature.commons.goals.screens.GoalWizardScreen
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitButton
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.onboard.components.ActiveGoalItem
import com.guaracode.evofit.presentation.ui.feature.onboard.components.AddNewGoalButton
import com.guaracode.evofit.presentation.ui.feature.onboard.components.GoalTag
import com.guaracode.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import com.guaracode.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject

@Composable
fun OnboardingGoalsScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
    tracker: OnboardingTracker = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        tracker.trackOnboardingGoalsScreenView()
    }

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
        androidx.compose.ui.window.Dialog(
            onDismissRequest = {
                showDialog = false
                selectedSuggestion = null
            },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
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
                        text = "Pular por enquanto",
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
                    text = "Continuar",
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
                    text = "Quais são suas metas?",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                Text(
                    text = "Defina objetivos para acompanhar seu progresso.",
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
                        onClick = {
                            selectedSuggestion = null
                            showDialog = true
                        }
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
