package com.example.evofit.presentation.ui.feature.workout.startworkout.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.ui.feature.workout.startworkout.session.ExerciseProgressState
import com.example.evofit.presentation.ui.feature.workout.startworkout.session.SetProgressState
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun ExerciseTrackingCard(
    exercise: ExerciseProgressState,
    index: Int,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    onToggleSetDone: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandClick() }
                    .padding(Dimens.SpacingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                Box(
                    modifier = Modifier
                        .size(Dimens.SpacingExtraExtraLarge)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val completedInExercise = exercise.sets.count { it.isDone }
                    Text(
                        text = stringResource(R.string.workout_start_exercise_progress, completedInExercise, exercise.sets.size),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rotation")
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.workout_start_expand_collapse_desc),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.rotate(rotationState)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dimens.SpacingMedium, end = Dimens.SpacingMedium, bottom = Dimens.SpacingMedium)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = Dimens.BorderWidthThin)
                    Spacer(modifier = Modifier.height(Dimens.SpacingMediumSmall))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.workout_start_column_set),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.weight(0.8f),
                            textAlign = TextAlign.Center
                        )
                        when (exercise.unit) {
                            MeasurementUnit.WEIGHT -> {
                                Text(
                                    stringResource(R.string.workout_start_column_weight),
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    stringResource(R.string.workout_start_column_reps),
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = TextAlign.Center
                                )
                            }
                            MeasurementUnit.DISTANCE -> {
                                Text(
                                    "Dist. (km)",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "Tempo (min)",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = TextAlign.Center
                                )
                            }
                            MeasurementUnit.TIME -> {
                                Text(
                                    "Tempo (min)",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.weight(2.4f),
                                    textAlign = TextAlign.Center
                                )
                            }
                            MeasurementUnit.REPS -> {
                                Text(
                                    "Reps",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.weight(2.4f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Text(
                            stringResource(R.string.workout_start_column_ok),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.weight(0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                    exercise.sets.forEach { setItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimens.AuthIllustrationSizeMedium / 3), // ~46.dp
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${setItem.setNumber}",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(0.8f),
                                textAlign = TextAlign.Center
                            )

                            when (exercise.unit) {
                                MeasurementUnit.WEIGHT -> {
                                    val weightStr = if (setItem.weight % 1 == 0.0) "${setItem.weight.toInt()}" else "${setItem.weight}"
                                    Text(
                                        text = "$weightStr kg",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.weight(1.2f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "${setItem.reps}",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.weight(1.2f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                MeasurementUnit.DISTANCE -> {
                                    val distStr = if ((setItem.distance ?: 0.0) % 1 == 0.0) "${setItem.distance?.toInt()}" else "${setItem.distance}"
                                    Text(
                                        text = "$distStr km",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.weight(1.2f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "${setItem.time} min",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.weight(1.2f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                MeasurementUnit.TIME -> {
                                    Text(
                                        text = "${setItem.time} min",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.weight(2.4f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                MeasurementUnit.REPS -> {
                                    Text(
                                        text = "${setItem.reps}",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.weight(2.4f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(0.8f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomCircularCheckbox(
                                    isChecked = setItem.isDone,
                                    onCheckedChange = {
                                        onToggleSetDone(exercise.workoutExerciseId, setItem.setNumber)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCircularCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(Dimens.AuthBadgeSizeDefault - Dimens.SpacingSmall) // ~28.dp
            .clip(CircleShape)
            .background(if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent)
            .border(
                width = Dimens.SpacingExtraExtraSmall,
                color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                shape = CircleShape
            )
            .clickable { onCheckedChange(!isChecked) },
        contentAlignment = Alignment.Center
    ) {
        if (isChecked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(Dimens.SpacingMedium)
            )
        }
    }
}

@Composable
fun HeaderIndicatorCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(Dimens.BottomNavigationHeight - Dimens.SpacingSmall) // ~72.dp
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black)
            )
            Spacer(modifier = Modifier.height(Dimens.SpacingExtraSmall))
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview(showBackground = true, name = "Start Workout Components")
@Composable
fun StartWorkoutComponentsPreview() {
    EvoFitTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens.SpacingMedium)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                Text("Indicators", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onBackground)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)) {
                    HeaderIndicatorCard(value = "00:45", label = "Tempo", modifier = Modifier.weight(1f))
                    HeaderIndicatorCard(value = "120", label = "Kcal", modifier = Modifier.weight(1f))
                    HeaderIndicatorCard(value = "2/8", label = "Séries", modifier = Modifier.weight(1f))
                }

                Text("Exercise Cards", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                
                ExerciseTrackingCard(
                    exercise = ExerciseProgressState(
                        workoutExerciseId = "1",
                        exerciseId = "e1",
                        name = "Supino Reto",
                        unit = MeasurementUnit.WEIGHT,
                        sets = listOf(
                            SetProgressState(setNumber = 1, weight = 60.0, reps = 12, isDone = true),
                            SetProgressState(setNumber = 2, weight = 60.0, reps = 10, isDone = false)
                        )
                    ),
                    index = 0,
                    isExpanded = true,
                    onExpandClick = {},
                    onToggleSetDone = { _, _ -> }
                )

                ExerciseTrackingCard(
                    exercise = ExerciseProgressState(
                        workoutExerciseId = "2",
                        exerciseId = "e2",
                        name = "Corrida na Esteira",
                        unit = MeasurementUnit.DISTANCE,
                        sets = listOf(
                            SetProgressState(setNumber = 1, weight = 0.0, reps = 0, distance = 2.5, time = 15, isDone = false)
                        )
                    ),
                    index = 1,
                    isExpanded = false,
                    onExpandClick = {},
                    onToggleSetDone = { _, _ -> }
                )

                Text("Checkboxes", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onBackground)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
                    CustomCircularCheckbox(isChecked = false, onCheckedChange = {})
                    CustomCircularCheckbox(isChecked = true, onCheckedChange = {})
                }
            }
        }
    }
}
