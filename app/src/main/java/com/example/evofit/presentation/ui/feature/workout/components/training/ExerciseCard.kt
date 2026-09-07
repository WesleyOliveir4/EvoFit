package com.example.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun MuscleGroupPreviewCard(
    groupName: String,
    exercises: List<ExercisePreviewItem>,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDragging: Boolean = false,
    dragOffset: () -> Float = { 0f },
    onDragStart: () -> Unit = {},
    onDrag: (Float) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
) {
    val currentOnDragStart by rememberUpdatedState(onDragStart)
    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnDragEnd by rememberUpdatedState(onDragEnd)
    val currentOnDragCancel by rememberUpdatedState(onDragCancel)

    val animatedElevation by animateDpAsState(if (isDragging) Dimens.SpacingMediumSmall else Dimens.SpacingNone, label = "elevation")
    val animatedScale by animateFloatAsState(if (isDragging) 1.02f else 1f, label = "scale")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = dragOffset()
                scaleX = animatedScale
                scaleY = animatedScale
                shadowElevation = animatedElevation.toPx()
            }
            .zIndex(if (isDragging) 10f else 1f)
            .border(
                width = if (isDragging) Dimens.BorderWidthThin else Dimens.ElevationNone,
                color = if (isDragging) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isDragging) { onExpandClick() }
                    .padding(Dimens.SpacingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = groupName.uppercase(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(R.string.workout_preview_exercises_count, exercises.size),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                val rotationState by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    label = "rotation"
                )

                Icon(
                    imageVector = Icons.Default.DragIndicator,
                    contentDescription = stringResource(R.string.main_workout_drag_handle_desc),
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .size(Dimens.IconSizeDefault)
                        .pointerInput(groupName) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = { currentOnDragStart() },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    currentOnDrag(dragAmount.y)
                                },
                                onDragEnd = { currentOnDragEnd() },
                                onDragCancel = { currentOnDragCancel() }
                            )
                        }
                )

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
                        .padding(start = Dimens.SpacingMedium, end = Dimens.SpacingMedium, bottom = Dimens.SpacingMedium),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                ) {
                    exercises.forEachIndexed { index, exercise ->
                        ExercisePreviewCard(index = index + 1, item = exercise)
                    }
                }
            }
        }
    }
}

@Composable
fun ExercisePreviewCard(
    index: Int,
    item: ExercisePreviewItem
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMediumSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.SpacingExtraExtraLarge)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$index",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = item.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val detailText = when (item.unit) {
                    MeasurementUnit.WEIGHT -> {
                        val weightStr = if (item.weight % 1 == 0.0) "${item.weight.toInt()}" else "${item.weight}"
                        "$weightStr kg × ${item.reps} reps"
                    }
                    MeasurementUnit.DISTANCE -> {
                        val distanceStr = if ((item.distance ?: 0.0) % 1 == 0.0) "${item.distance?.toInt()}" else "${item.distance}"
                        "$distanceStr km × ${item.time ?: 0} min"
                    }
                    MeasurementUnit.TIME -> {
                        "${item.time ?: 0} min"
                    }
                    MeasurementUnit.REPS -> {
                        "${item.reps} reps"
                    }
                }
                Text(
                    text = stringResource(
                        R.string.main_workout_exercise_series_format,
                        stringResource(R.string.main_workout_series_count, item.setsCount),
                        detailText
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExercisePreviewCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            ExercisePreviewCard(
                index = 1,
                item = ExercisePreviewItem(
                    workoutExerciseId = "1",
                    name = "Supino Reto",
                    setsCount = 3,
                    weight = 60.0,
                    reps = 10,
                    unit = MeasurementUnit.WEIGHT
                )
            )
        }
    }
}
