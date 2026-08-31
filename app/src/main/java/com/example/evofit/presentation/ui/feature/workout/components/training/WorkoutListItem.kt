package com.example.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.FitnessCenter
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.model.WorkoutUIModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun WorkoutListItem(
    workout: WorkoutUIModel,
    modifier: Modifier = Modifier,
    isDragging: Boolean = false,
    dragOffset: () -> Float = { 0f },
    onDragStart: () -> Unit = {},
    onDrag: (Float) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val currentOnDragStart by rememberUpdatedState(onDragStart)
    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnDragEnd by rememberUpdatedState(onDragEnd)
    val currentOnDragCancel by rememberUpdatedState(onDragCancel)

    val animatedElevation by animateDpAsState(if (isDragging) Dimens.SpacingMediumSmall else Dimens.SpacingNone, label = "elevation")
    val animatedScale by animateFloatAsState(if (isDragging) 1.05f else 1f, label = "scale")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = dragOffset()
                scaleX = animatedScale
                scaleY = animatedScale
                shadowElevation = animatedElevation.toPx()
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
                clip = true
            }
            .border(
                width = if (isDragging) Dimens.BorderWidthThin else Dimens.ElevationNone,
                color = if (isDragging) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            )
            .clickable(enabled = !isDragging) { onClick() },
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(Dimens.SpacingMediumSmall)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(Dimens.OnboardingIconSize / 2) // Roughly 40.dp
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(Dimens.SpacingMediumSmall))
                    .clip(RoundedCornerShape(Dimens.SpacingMediumSmall)),
                contentAlignment = Alignment.Center
            ) {
                if (workout.imageRes != null) {
                    Icon(
                        painter = painterResource(id = workout.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(Dimens.SpacingExtraSmall)
                            .fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.StatCardIconSize)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Dimens.SpacingMedium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val exercisesStr = stringResource(R.string.main_workout_exercise_count, workout.exercises)
                val seriesStr = stringResource(R.string.main_workout_series_count, workout.series)
                Text(
                    text = stringResource(R.string.main_workout_exercise_series_format, exercisesStr, seriesStr),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = stringResource(R.string.main_workout_drag_handle_desc),
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(Dimens.IconSizeDefault)
                    .pointerInput(workout.id) {
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

            Spacer(modifier = Modifier.width(Dimens.SpacingSmall))

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutListItemPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            WorkoutListItem(
                workout = WorkoutUIModel(
                    id = "1",
                    title = "Treino de Peito",
                    exercises = 5,
                    series = 15,
                    imageRes = com.example.evofit.R.drawable.img_chest
                ),
                onClick = {}
            )
        }
    }
}
