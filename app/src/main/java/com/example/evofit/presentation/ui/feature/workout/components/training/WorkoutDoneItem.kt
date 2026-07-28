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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.model.WorkoutHistoryUIModel
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
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = workoutDone.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${workoutDone.date} • ${workoutDone.time}",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                workoutDone.exercises.forEachIndexed { index, exerciseItem ->
                    ExercisePreviewCard(
                        index = index + 1,
                        item = exerciseItem
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutDoneItemPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(16.dp)) {
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
        Box(modifier = Modifier.padding(16.dp)) {
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
