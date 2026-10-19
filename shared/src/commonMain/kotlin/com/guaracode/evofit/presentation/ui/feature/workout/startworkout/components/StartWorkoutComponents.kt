package com.guaracode.evofit.presentation.ui.feature.workout.startworkout.components

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.session.ExerciseProgressState
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.session.SetProgressState
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun ExerciseTrackingCard(
    exercise: ExerciseProgressState,
    index: Int,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    onToggleSetDone: (String, Int) -> Unit,
    onInfoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                    modifier = Modifier.size(Dimens.SpacingExtraExtraLarge + 4.dp),
                    contentAlignment = Alignment.Center
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

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(Dimens.IconSizeSmall)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .clip(CircleShape)
                            .clickable { onInfoClick(exercise.exerciseId) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Informação",
                            tint = Color.Black,
                            modifier = Modifier.size(Dimens.SpacingMediumSmall)
                        )
                    }
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
                        text = "$completedInExercise de ${exercise.sets.size} séries concluídas",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rotation")
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
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
                            "SÉRIE",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.weight(0.8f),
                            textAlign = TextAlign.Center
                        )
                        when (exercise.unit) {
                            MeasurementUnit.WEIGHT -> {
                                Text(
                                    "CARGA",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "REPS",
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
                            "OK",
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
                                .height(Dimens.AuthIllustrationSizeMedium / 3),
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
            .size(Dimens.AuthBadgeSizeDefault - Dimens.SpacingSmall)
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
        modifier = modifier.height(Dimens.BottomNavigationHeight - Dimens.SpacingSmall),
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

@Preview
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
                ExerciseTrackingCard(
                    exercise = ExerciseProgressState(
                        workoutExerciseId = "1",
                        exerciseId = "e1",
                        name = "Supino Reto",
                        unit = MeasurementUnit.WEIGHT,
                        sets = listOf(
                            SetProgressState(setNumber = 1, weight = 60.0, reps = 12, isDone = true)
                        )
                    ),
                    index = 0,
                    isExpanded = true,
                    onExpandClick = {},
                    onToggleSetDone = { _, _ -> },
                    onInfoClick = {}
                )
            }
        }
    }
}
