package com.example.evofit.presentation.ui.feature.commons.goals.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.presentation.ui.feature.commons.goals.components.ExerciseStep
import com.example.evofit.presentation.ui.feature.commons.goals.components.GoalTypeStep
import com.example.evofit.presentation.ui.feature.commons.goals.components.GoalValueStep
import com.example.evofit.presentation.ui.feature.commons.goals.components.MuscleGroupStep
import com.example.evofit.presentation.ui.feature.commons.goals.components.WizardProgress
import com.example.evofit.presentation.ui.feature.commons.goals.components.WizardTopBar
import com.example.evofit.presentation.ui.feature.commons.goals.viewmodel.GoalAction
import com.example.evofit.presentation.ui.feature.commons.goals.viewmodel.GoalWizardStep
import com.example.evofit.presentation.ui.feature.commons.goals.viewmodel.GoalWizardUiState
import com.example.evofit.presentation.ui.feature.commons.goals.viewmodel.GoalWizardViewModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun GoalWizardScreen(
    onBack: () -> Unit,
    onClose: () -> Unit,
    onGoalConfirmed: (UserGoal) -> Unit,
    initialSuggestion: GoalSuggestion? = null,
    viewModel: GoalWizardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    val strengthLabel = stringResource(R.string.goal_category_strength)
    val resistanceLabel = stringResource(R.string.goal_category_resistance)
    val weightLossLabel = stringResource(R.string.goal_category_weight_loss)
    val muscleGainLabel = stringResource(R.string.goal_category_muscle_gain)

    LaunchedEffect(initialSuggestion) {
        viewModel.initWithSuggestion(
            initialSuggestion,
            strengthLabel,
            resistanceLabel,
            weightLossLabel,
            muscleGainLabel
        )
    }

    LaunchedEffect(uiState.confirmedGoal) {
        uiState.confirmedGoal?.let {
            onGoalConfirmed(it)
            viewModel.onAction(GoalAction.ResetConfirmedGoal)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onAction(GoalAction.Reset)
        }
    }

    GoalWizardScreenContent(
        uiState = uiState,
        onAction = { viewModel.onAction(it) },
        onClose = onClose,
        strengthLabel = strengthLabel
    )
}

@Composable
fun GoalWizardScreenContent(
    uiState: GoalWizardUiState,
    onAction: (GoalAction) -> Unit,
    onClose: () -> Unit,
    strengthLabel: String
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            GoalWizardContent(
                state = uiState,
                onAction = onAction,
                onClose = onClose,
                strengthLabel = strengthLabel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalWizardBottomSheet(
    onDismiss: () -> Unit,
    onGoalConfirmed: (UserGoal) -> Unit,
    initialSuggestion: GoalSuggestion? = null,
    viewModel: GoalWizardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    val strengthLabel = stringResource(R.string.goal_category_strength)
    val resistanceLabel = stringResource(R.string.goal_category_resistance)
    val weightLossLabel = stringResource(R.string.goal_category_weight_loss)
    val muscleGainLabel = stringResource(R.string.goal_category_muscle_gain)

    LaunchedEffect(initialSuggestion) {
        viewModel.initWithSuggestion(
            initialSuggestion,
            strengthLabel,
            resistanceLabel,
            weightLossLabel,
            muscleGainLabel
        )
    }

    LaunchedEffect(uiState.confirmedGoal) {
        uiState.confirmedGoal?.let {
            onGoalConfirmed(it)
            viewModel.onAction(GoalAction.ResetConfirmedGoal)
            onDismiss()
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = Dimens.SpacingSmall,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = Dimens.SpacingLarge, topEnd = Dimens.SpacingLarge)
    ) {
        GoalWizardContent(
            state = uiState,
            onAction = { viewModel.onAction(it) },
            onClose = onDismiss,
            strengthLabel = strengthLabel
        )
    }
}

@Composable
fun GoalWizardContent(
    state: GoalWizardUiState,
    onAction: (GoalAction) -> Unit,
    onClose: () -> Unit,
    strengthLabel: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f))

        Column(
            modifier = Modifier.padding(Dimens.SpacingLarge).weight(1f)
        ) {
            WizardTopBar(
                onBack = { onAction(GoalAction.Back) },
                onClose = onClose,
                showBack = state.currentStep != GoalWizardStep.GOAL_TYPE
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            WizardProgress(state.currentStep)

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            AnimatedContent(
                targetState = state.currentStep,
                label = "StepTransition",
                transitionSpec = {
                    if (targetState.ordinal > initialState.ordinal) {
                        slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                    } else {
                        slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                    }
                }
            ) { step ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    when (step) {
                        GoalWizardStep.GOAL_TYPE -> GoalTypeStep(onAction)
                        GoalWizardStep.MUSCLE_GROUP -> {
                            val isStrength = state.selectedCategory == strengthLabel
                            val filteredGroups = if (isStrength) {
                                state.muscleGroups.filter { it.category == ExerciseCategory.STRENGTH }
                            } else {
                                state.muscleGroups.filter { it.category == ExerciseCategory.CARDIO }
                            }
                            MuscleGroupStep(filteredGroups, onAction)
                        }
                        GoalWizardStep.EXERCISE -> {
                            ExerciseStep(state.filteredExercises, state.search, onAction)
                        }
                        GoalWizardStep.GOAL_VALUE -> {
                            GoalValueStep(
                                selectedExercise = state.selectedExercise,
                                value = state.goalValue,
                                timeValue = state.timeValue,
                                onAction = onAction
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalWizardScreenPreview() {
    val sampleMuscleGroups = listOf(
        MuscleGroup(id = "1", name = "Peito", category = ExerciseCategory.STRENGTH),
        MuscleGroup(id = "2", name = "Costas", category = ExerciseCategory.STRENGTH),
        MuscleGroup(id = "3", name = "Corrida", category = ExerciseCategory.CARDIO)
    )

    val sampleExercises = listOf(
        Exercise(id = "1", name = "Supino Reto", muscleGroupId = "1", unit = MeasurementUnit.WEIGHT),
        Exercise(id = "2", name = "Remada Curvada", muscleGroupId = "2", unit = MeasurementUnit.WEIGHT),
        Exercise(id = "3", name = "Corrida na Esteira", muscleGroupId = "3", unit = MeasurementUnit.DISTANCE)
    )

    EvoFitTheme {
        GoalWizardScreenContent(
            uiState = GoalWizardUiState(
                currentStep = GoalWizardStep.GOAL_TYPE,
                muscleGroups = sampleMuscleGroups,
                filteredExercises = sampleExercises
            ),
            onAction = {},
            onClose = {},
            strengthLabel = "Força"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalWizardScreenMuscleGroupPreview() {
    val sampleMuscleGroups = listOf(
        MuscleGroup(id = "1", name = "Peito", category = ExerciseCategory.STRENGTH),
        MuscleGroup(id = "2", name = "Costas", category = ExerciseCategory.STRENGTH),
        MuscleGroup(id = "3", name = "Corrida", category = ExerciseCategory.CARDIO)
    )

    val sampleExercises = listOf(
        Exercise(id = "1", name = "Supino Reto", muscleGroupId = "1", unit = MeasurementUnit.WEIGHT),
        Exercise(id = "2", name = "Remada Curvada", muscleGroupId = "2", unit = MeasurementUnit.WEIGHT),
        Exercise(id = "3", name = "Corrida na Esteira", muscleGroupId = "3", unit = MeasurementUnit.DISTANCE)
    )

    EvoFitTheme {
        GoalWizardScreenContent(
            uiState = GoalWizardUiState(
                currentStep = GoalWizardStep.MUSCLE_GROUP,
                selectedCategory = "Força",
                muscleGroups = sampleMuscleGroups,
                filteredExercises = sampleExercises
            ),
            onAction = {},
            onClose = {},
            strengthLabel = "Força"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalWizardScreenCardioGoalPreview() {
    val sampleExercise = Exercise(id = "3", name = "Corrida na Esteira", muscleGroupId = "3", unit = MeasurementUnit.DISTANCE)

    EvoFitTheme {
        GoalWizardScreenContent(
            uiState = GoalWizardUiState(
                currentStep = GoalWizardStep.GOAL_VALUE,
                selectedCategory = "Resistência",
                selectedExercise = sampleExercise,
                goalValue = "5",
                timeValue = "30"
            ),
            onAction = {},
            onClose = {},
            strengthLabel = "Força"
        )
    }
}
