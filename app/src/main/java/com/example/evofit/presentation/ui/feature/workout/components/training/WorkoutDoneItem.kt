package com.example.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.model.WorkoutHistoryUIModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun WorkoutDoneItem(
    workoutDone: WorkoutHistoryUIModel,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            )
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(Dimens.SpacingMediumSmall)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workoutDone.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${workoutDone.date} • ${workoutDone.time}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                workoutDone.exercises.forEachIndexed { index, exerciseItem ->
                    ExercisePreviewCard(
                        index = index + 1,
                        item = exerciseItem
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutDoneItemPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            WorkoutDoneItem(
                workoutDone = WorkoutHistoryUIModel(
                    id = "1",
                    name = "Treino de Peito",
                    date = "28/07/2026",
                    time = "12:00",
                    exercises = listOf(
                        ExercisePreviewItem(
                            workoutExerciseId = "1",
                            name = "Supino Reto",
                            setsCount = 4,
                            weight = 80.0,
                            reps = 10,
                            unit = MeasurementUnit.WEIGHT
                        ),
                        ExercisePreviewItem(
                            workoutExerciseId = "2",
                            name = "Supino Inclinado",
                            setsCount = 3,
                            weight = 60.0,
                            reps = 12,
                            unit = MeasurementUnit.WEIGHT
                        )
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutDoneItemExpandedPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            WorkoutDoneItem(
                initiallyExpanded = true,
                workoutDone = WorkoutHistoryUIModel(
                    id = "1",
                    name = "Treino de Peito",
                    date = "28/07/2026",
                    time = "12:00",
                    exercises = listOf(
                        ExercisePreviewItem(
                            workoutExerciseId = "1",
                            name = "Supino Reto",
                            setsCount = 4,
                            weight = 80.0,
                            reps = 10,
                            unit = MeasurementUnit.WEIGHT
                        ),
                        ExercisePreviewItem(
                            workoutExerciseId = "2",
                            name = "Supino Inclinado",
                            setsCount = 3,
                            weight = 60.0,
                            reps = 12,
                            unit = MeasurementUnit.WEIGHT
                        )
                    )
                )
            )
        }
    }
}
