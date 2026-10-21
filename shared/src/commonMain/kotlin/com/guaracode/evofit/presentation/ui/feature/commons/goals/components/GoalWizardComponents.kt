package com.guaracode.evofit.presentation.ui.feature.commons.goals.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.guaracode.evofit.domain.model.*
import com.guaracode.evofit.presentation.ui.feature.commons.goals.viewmodel.GoalAction
import com.guaracode.evofit.presentation.ui.feature.commons.goals.viewmodel.GoalWizardStep
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.goal_category_muscle_gain
import evofit.shared.generated.resources.goal_category_resistance
import evofit.shared.generated.resources.goal_category_strength
import evofit.shared.generated.resources.goal_category_weight_loss
import evofit.shared.generated.resources.goal_dialog_choose_type
import evofit.shared.generated.resources.goal_dialog_title_new
import evofit.shared.generated.resources.goal_search_placeholder
import evofit.shared.generated.resources.goal_step_cardio_distance
import evofit.shared.generated.resources.goal_step_exercise
import evofit.shared.generated.resources.goal_step_muscle_group
import evofit.shared.generated.resources.goal_step_reps
import evofit.shared.generated.resources.goal_step_time
import evofit.shared.generated.resources.goal_step_weight
import evofit.shared.generated.resources.goal_unit_kg
import evofit.shared.generated.resources.goal_unit_km
import evofit.shared.generated.resources.goal_unit_min
import evofit.shared.generated.resources.goal_unit_reps
import evofit.shared.generated.resources.onboarding_back
import evofit.shared.generated.resources.onboarding_button_continue
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WizardTopBar(onBack: () -> Unit, onClose: () -> Unit, showBack: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            Text(
                text = stringResource(Res.string.onboarding_back),
                modifier = Modifier.clickable { onBack() },
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            Spacer(modifier = Modifier.width(Dimens.SpacingExtraExtraLarge))
        }

        Text(
            text = stringResource(Res.string.goal_dialog_title_new),
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
        steps.forEachIndexed { index, _ ->
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
    val strengthLabel = stringResource(Res.string.goal_category_strength)
    val resistanceLabel = stringResource(Res.string.goal_category_resistance)
    val weightLossLabel = stringResource(Res.string.goal_category_weight_loss)
    val muscleGainLabel = stringResource(Res.string.goal_category_muscle_gain)

    val categories = listOf(
        strengthLabel,
        resistanceLabel,
        weightLossLabel,
        muscleGainLabel
    )

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)) {
        Text(
            text = stringResource(Res.string.goal_dialog_choose_type),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )
        categories.forEach { category ->
            Button(
                onClick = {
                    val isWeight = category == weightLossLabel || category == muscleGainLabel
                    onAction(GoalAction.SelectCategory(category, isWeight))
                },
                modifier = Modifier.fillMaxWidth().height(Dimens.ButtonHeightPrimary),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
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
            text = stringResource(Res.string.goal_step_muscle_group),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(muscleGroups) { group ->
                Surface(
                    onClick = { onAction(GoalAction.SelectMuscle(group)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
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
            text = stringResource(Res.string.goal_step_exercise),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = search,
            onValueChange = { onAction(GoalAction.UpdateSearch(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(Res.string.goal_search_placeholder)) },
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
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(filtered) { exercise ->
                Surface(
                    onClick = { onAction(GoalAction.SelectExercise(exercise)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
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
fun GoalValueStep(
    selectedExercise: Exercise?,
    value: String,
    timeValue: String = "",
    onAction: (GoalAction) -> Unit
) {
    val label = when (selectedExercise?.unit) {
        MeasurementUnit.REPS -> stringResource(Res.string.goal_step_reps)
        MeasurementUnit.TIME -> stringResource(Res.string.goal_step_time)
        MeasurementUnit.DISTANCE -> stringResource(Res.string.goal_step_cardio_distance)
        else -> stringResource(Res.string.goal_step_weight)
    }

    val suffix = when (selectedExercise?.unit) {
        MeasurementUnit.REPS -> stringResource(Res.string.goal_unit_reps)
        MeasurementUnit.TIME -> stringResource(Res.string.goal_unit_min)
        MeasurementUnit.DISTANCE -> stringResource(Res.string.goal_unit_km)
        else -> stringResource(Res.string.goal_unit_kg)
    }

    val isCardioDistance = selectedExercise?.unit == MeasurementUnit.DISTANCE

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

        if (isCardioDistance) {
            Text(
                text = stringResource(Res.string.goal_step_time),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )

            OutlinedTextField(
                value = timeValue,
                onValueChange = { onAction(GoalAction.UpdateTimeValue(it)) },
                modifier = Modifier.fillMaxWidth(),
                suffix = { Text(stringResource(Res.string.goal_unit_min)) },
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
        }

        Button(
            onClick = { onAction(GoalAction.Confirm) },
            enabled = value.isNotBlank() && (!isCardioDistance || timeValue.isNotBlank()),
            modifier = Modifier.fillMaxWidth().height(Dimens.ButtonHeightPrimary),
            shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
            Text(stringResource(Res.string.onboarding_button_continue), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Preview
@Composable
private fun WizardTopBarPreview() {
    EvoFitTheme {
        WizardTopBar(
            onBack = {},
            onClose = {},
            showBack = true
        )
    }
}

@Preview
@Composable
private fun WizardProgressPreview() {
    EvoFitTheme {
        Column(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            WizardProgress(currentStep = GoalWizardStep.GOAL_TYPE)
            WizardProgress(currentStep = GoalWizardStep.MUSCLE_GROUP)
            WizardProgress(currentStep = GoalWizardStep.EXERCISE)
            WizardProgress(currentStep = GoalWizardStep.GOAL_VALUE)
        }
    }
}
