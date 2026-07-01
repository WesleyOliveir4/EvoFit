package com.example.evofit.presentation.ui.feature.workout.startworkout.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.workout.components.CustomCircularCheckbox
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.ExerciseProgressState

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
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    val completedInExercise = exercise.sets.count { it.isDone }
                    Text(
                        text = stringResource(R.string.workout_start_exercise_progress, completedInExercise, exercise.sets.size),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
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
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.workout_start_column_set), color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Text(stringResource(R.string.workout_start_column_weight), color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
                        Text(stringResource(R.string.workout_start_column_reps), color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
                        Text(stringResource(R.string.workout_start_column_ok), color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    exercise.sets.forEach { setItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${setItem.setNumber}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                            val weightStr = if (setItem.weight % 1 == 0.0) "${setItem.weight.toInt()}" else "${setItem.weight}"
                            Text(
                                text = stringResource(R.string.workout_start_weight_kg, weightStr),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.weight(1.2f),
                                textAlign = TextAlign.Center
                            )
                            Text(text = "${setItem.reps}", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomCircularCheckbox(
                                    isChecked = setItem.isDone,
                                    onCheckedChange = {
                                        onToggleSetDone(exercise.id, setItem.setNumber)
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
