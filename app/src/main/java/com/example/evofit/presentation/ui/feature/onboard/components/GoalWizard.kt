package com.example.evofit.presentation.ui.feature.onboard.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.R
import com.example.evofit.domain.model.*
import java.util.*

enum class GoalWizardStep {
    GOAL_TYPE,
    MUSCLE_GROUP,
    EXERCISE,
    GOAL_VALUE
}

data class GoalWizardUiState(
    val currentStep: GoalWizardStep = GoalWizardStep.GOAL_TYPE,
    val selectedCategory: String? = null,
    val selectedMuscle: MuscleGroup? = null,
    val selectedExercise: Exercise? = null,
    val goalValue: String = "",
    val search: String = ""
)

sealed class GoalAction {
    data class SelectCategory(val category: String) : GoalAction()
    data class SelectMuscle(val muscle: MuscleGroup) : GoalAction()
    data class SelectExercise(val exercise: Exercise) : GoalAction()
    data class UpdateValue(val value: String) : GoalAction()
    data class UpdateSearch(val search: String) : GoalAction()
    data object Confirm : GoalAction()
    data object Back : GoalAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalWizardBottomSheet(
    muscleGroups: List<MuscleGroup>,
    getExercises: (String) -> List<Exercise>,
    onDismiss: () -> Unit,
    onGoalConfirmed: (UserGoal) -> Unit,
    initialSuggestion: GoalSuggestion? = null
) {
    val strengthLabel = stringResource(R.string.goal_category_strength)
    val resistanceLabel = stringResource(R.string.goal_category_resistance)
    val weightLossLabel = stringResource(R.string.goal_category_weight_loss)
    val muscleGainLabel = stringResource(R.string.goal_category_muscle_gain)

    var state by remember(initialSuggestion) {
        val category = initialSuggestion?.let {
            when {
                it.isWeightGoal -> {
                    // Try to guess based on text if category not explicit, but suggestions usually have context
                    if (it.text.contains("perder", true) || it.text.contains("peso", true)) weightLossLabel 
                    else muscleGainLabel
                }
                it.category == ExerciseCategory.STRENGTH -> strengthLabel
                it.category == ExerciseCategory.CARDIO -> resistanceLabel
                else -> null
            }
        }
        
        val muscle = initialSuggestion?.muscleGroupId?.let { id ->
            muscleGroups.find { it.id == id }
        }
        
        val exercise = initialSuggestion?.exerciseId?.let { id ->
            muscle?.let { group -> 
                getExercises(group.id).find { it.id == id } 
            }
        }

        val step = when {
            initialSuggestion == null -> GoalWizardStep.GOAL_TYPE
            initialSuggestion.isWeightGoal -> GoalWizardStep.GOAL_VALUE
            exercise != null -> GoalWizardStep.GOAL_VALUE
            muscle != null -> GoalWizardStep.EXERCISE
            category != null -> GoalWizardStep.MUSCLE_GROUP
            else -> GoalWizardStep.GOAL_TYPE
        }

        mutableStateOf(
            GoalWizardUiState(
                currentStep = step,
                selectedCategory = category,
                selectedMuscle = muscle,
                selectedExercise = exercise
            )
        )
    }

    val onAction: (GoalAction) -> Unit = { action ->
        when (action) {
            is GoalAction.SelectCategory -> {
                val isWeight = action.category == weightLossLabel || 
                               action.category == muscleGainLabel
                
                state = state.copy(
                    selectedCategory = action.category,
                    currentStep = if (isWeight) GoalWizardStep.GOAL_VALUE else GoalWizardStep.MUSCLE_GROUP
                )
            }
            is GoalAction.SelectMuscle -> {
                state = state.copy(
                    selectedMuscle = action.muscle,
                    selectedExercise = null,
                    currentStep = GoalWizardStep.EXERCISE
                )
            }
            is GoalAction.SelectExercise -> {
                state = state.copy(
                    selectedExercise = action.exercise,
                    currentStep = GoalWizardStep.GOAL_VALUE
                )
            }
            is GoalAction.UpdateValue -> state = state.copy(goalValue = action.value)
            is GoalAction.UpdateSearch -> state = state.copy(search = action.search)
            GoalAction.Back -> {
                state = when (state.currentStep) {
                    GoalWizardStep.GOAL_TYPE -> state
                    GoalWizardStep.MUSCLE_GROUP -> state.copy(currentStep = GoalWizardStep.GOAL_TYPE)
                    GoalWizardStep.EXERCISE -> state.copy(currentStep = GoalWizardStep.MUSCLE_GROUP)
                    GoalWizardStep.GOAL_VALUE -> {
                        val isWeight = state.selectedCategory == weightLossLabel || 
                                       state.selectedCategory == muscleGainLabel
                        if (isWeight) state.copy(currentStep = GoalWizardStep.GOAL_TYPE)
                        else state.copy(currentStep = GoalWizardStep.EXERCISE)
                    }
                }
            }
            GoalAction.Confirm -> {
                val goal = when {
                    state.selectedCategory == weightLossLabel || 
                    state.selectedCategory == muscleGainLabel -> {
                        UserGoal.Weight(UUID.randomUUID().toString(), state.goalValue)
                    }
                    state.selectedExercise != null -> {
                        if (state.selectedMuscle?.category == ExerciseCategory.CARDIO) {
                             UserGoal.Cardio(
                                id = UUID.randomUUID().toString(),
                                type = state.selectedExercise!!.name,
                                distance = if (state.selectedExercise!!.unit == MeasurementUnit.DISTANCE) state.goalValue else null,
                                time = if (state.selectedExercise!!.unit == MeasurementUnit.TIME) state.goalValue else "0"
                            )
                        } else {
                            UserGoal.Strength(
                                id = UUID.randomUUID().toString(),
                                exerciseName = state.selectedExercise!!.name,
                                value = state.goalValue,
                                unit = state.selectedExercise!!.unit
                            )
                        }
                    }
                    else -> null
                }
                goal?.let {
                    onGoalConfirmed(it)
                    onDismiss()
                }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = Dimens.SpacingSmall,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(topStart = Dimens.SpacingLarge, topEnd = Dimens.SpacingLarge)
    ) {
        GoalWizardContent(
            state = state,
            muscleGroups = muscleGroups,
            getExercises = getExercises,
            onAction = onAction,
            onClose = onDismiss
        )
    }
}

@Composable
fun GoalWizardContent(
    state: GoalWizardUiState,
    muscleGroups: List<MuscleGroup>,
    getExercises: (String) -> List<Exercise>,
    onAction: (GoalAction) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f))

        Column(
            modifier = Modifier.padding(Dimens.SpacingLarge).weight(1f)
        ) {
            WizardTopBar(onBack = { onAction(GoalAction.Back) }, onClose = onClose, showBack = state.currentStep != GoalWizardStep.GOAL_TYPE)

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
                            val strengthText = stringResource(R.string.goal_category_strength)
                            val isStrength = state.selectedCategory == strengthText
                            val filteredGroups = if (isStrength) {
                                muscleGroups.filter { it.category == ExerciseCategory.STRENGTH }
                            } else {
                                muscleGroups.filter { it.category == ExerciseCategory.CARDIO }
                            }
                            MuscleGroupStep(filteredGroups, onAction)
                        }
                        GoalWizardStep.EXERCISE -> {
                            val exercises = state.selectedMuscle?.let { getExercises(it.id) } ?: emptyList()
                            ExerciseStep(exercises, state.search, onAction)
                        }
                        GoalWizardStep.GOAL_VALUE -> {
                            GoalValueStep(state.selectedExercise, state.goalValue, onAction)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
    }
}

@Composable
fun WizardTopBar(onBack: () -> Unit, onClose: () -> Unit, showBack: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            Text(
                text = stringResource(R.string.onboarding_back),
                modifier = Modifier.clickable { onBack() },
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            Spacer(modifier = Modifier.width(Dimens.SpacingExtraExtraLarge))
        }

        Text(
            text = stringResource(R.string.goal_dialog_title_new),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun WizardProgress(currentStep: GoalWizardStep) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val steps = GoalWizardStep.entries
        steps.forEachIndexed { index, step ->
            val isCompleted = index < currentStep.ordinal
            val isCurrent = index == currentStep.ordinal

        Box(
            modifier = Modifier
                .size(Dimens.SpacingMediumSmall)
                .clip(CircleShape)
                .background(
                    if (isCompleted || isCurrent) MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.surface
                )
                .border(
                    width = if (isCurrent) Dimens.SpacingExtraExtraSmall else Dimens.SpacingNone,
                    color = if (isCurrent) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                    shape = CircleShape
                )
            )

            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .width(Dimens.SpacingExtraExtraLarge)
                        .height(Dimens.SpacingExtraExtraSmall)
                        .background(
                            if (isCompleted) MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.surface
                        )
                )
            }
        }
    }
}

@Composable
fun GoalTypeStep(onAction: (GoalAction) -> Unit) {
    val categories = listOf(
        stringResource(R.string.goal_category_strength),
        stringResource(R.string.goal_category_resistance),
        stringResource(R.string.goal_category_weight_loss),
        stringResource(R.string.goal_category_muscle_gain)
    )

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)) {
        Text(
            text = stringResource(R.string.goal_dialog_choose_type),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )
        categories.forEach { category ->
            Button(
                onClick = { onAction(GoalAction.SelectCategory(category)) },
                modifier = Modifier.fillMaxWidth().height(Dimens.ButtonHeightPrimary),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            ) {
                Text(category, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
            }
        }
    }
}

@Composable
fun MuscleGroupStep(muscleGroups: List<MuscleGroup>, onAction: (GoalAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)) {
        Text(
            text = stringResource(R.string.goal_step_muscle_group),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
            modifier = Modifier.heightIn(max = Dimens.PreviewHeightLarge)
        ) {
            items(muscleGroups) { group ->
                    Surface(
                    onClick = { onAction(GoalAction.SelectMuscle(group)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(
                        text = group.name,
                        modifier = Modifier.padding(Dimens.SpacingMedium),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseStep(exercises: List<Exercise>, search: String, onAction: (GoalAction) -> Unit) {
    val filtered = remember(exercises, search) {
        if (search.isBlank()) exercises
        else exercises.filter { it.name.contains(search, ignoreCase = true) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)) {
        Text(
            text = stringResource(R.string.goal_step_exercise),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = search,
            onValueChange = { onAction(GoalAction.UpdateSearch(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.goal_search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
            modifier = Modifier.heightIn(max = Dimens.OnboardingLogoSize + Dimens.AuthLogoSizeOnboarding) // Custom max height or use constants
        ) {
            items(filtered) { exercise ->
                    Surface(
                    onClick = { onAction(GoalAction.SelectExercise(exercise)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(
                        text = exercise.name,
                        modifier = Modifier.padding(Dimens.SpacingMedium),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}

@Composable
fun GoalValueStep(selectedExercise: Exercise?, value: String, onAction: (GoalAction) -> Unit) {
    val label = when (selectedExercise?.unit) {
        MeasurementUnit.REPS -> stringResource(R.string.goal_step_reps)
        MeasurementUnit.TIME -> stringResource(R.string.goal_step_time)
        MeasurementUnit.DISTANCE -> stringResource(R.string.goal_step_cardio_distance)
        else -> stringResource(R.string.goal_step_weight)
    }

    val suffix = when (selectedExercise?.unit) {
        MeasurementUnit.REPS -> stringResource(R.string.goal_unit_reps)
        MeasurementUnit.TIME -> stringResource(R.string.goal_unit_min)
        MeasurementUnit.DISTANCE -> stringResource(R.string.goal_unit_km)
        else -> stringResource(R.string.goal_unit_kg)
    }

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = value,
            onValueChange = { onAction(GoalAction.UpdateValue(it)) },
            modifier = Modifier.fillMaxWidth(),
            suffix = { Text(suffix) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Button(
            onClick = { onAction(GoalAction.Confirm) },
            enabled = value.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(Dimens.ButtonHeightPrimary),
            shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
            Text(stringResource(R.string.onboarding_button_continue), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalWizardTypePreview() {
    EvoFitTheme {
        GoalWizardContent(
            state = GoalWizardUiState(currentStep = GoalWizardStep.GOAL_TYPE),
            muscleGroups = emptyList(),
            getExercises = { emptyList() },
            onAction = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoalWizardMuscleGroupPreview() {
    EvoFitTheme {
        GoalWizardContent(
            state = GoalWizardUiState(
                currentStep = GoalWizardStep.MUSCLE_GROUP,
                selectedCategory = stringResource(R.string.goal_category_strength)
            ),
            muscleGroups = listOf(
                MuscleGroup("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH),
                MuscleGroup("2", "Costas", MuscleGroupType.BACK, ExerciseCategory.STRENGTH)
            ),
            getExercises = { emptyList() },
            onAction = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoalWizardExercisePreview() {
    EvoFitTheme {
        GoalWizardContent(
            state = GoalWizardUiState(
                currentStep = GoalWizardStep.EXERCISE,
                selectedMuscle = MuscleGroup("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
            ),
            muscleGroups = emptyList(),
            getExercises = { 
                listOf(
                    Exercise("1", "Supino Reto", "1"),
                    Exercise("2", "Supino Inclinado", "1")
                )
            },
            onAction = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoalWizardGoalValuePreview() {
    EvoFitTheme {
        GoalWizardContent(
            state = GoalWizardUiState(
                currentStep = GoalWizardStep.GOAL_VALUE,
                selectedExercise = Exercise("1", "Supino Reto", "1", MeasurementUnit.WEIGHT)
            ),
            muscleGroups = emptyList(),
            getExercises = { emptyList() },
            onAction = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WizardProgressPreview() {
    EvoFitTheme {
        Column(modifier = Modifier.padding(Dimens.SpacingMedium), verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
            WizardProgress(GoalWizardStep.GOAL_TYPE)
            WizardProgress(GoalWizardStep.MUSCLE_GROUP)
            WizardProgress(GoalWizardStep.EXERCISE)
            WizardProgress(GoalWizardStep.GOAL_VALUE)
        }
    }
}
