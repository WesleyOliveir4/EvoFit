package com.example.evofit.presentation.ui.feature.workout.startworkout.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.workout.startworkout.components.ExerciseTrackingCard
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.ExerciseProgressState
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.SetProgressState
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutStartUiState
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutStartViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WorkoutStartScreen(
    workoutId: Int,
    viewModel: WorkoutStartViewModel = koinViewModel(parameters = { parametersOf(workoutId) }),
    onFinishWorkoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        WorkoutStartContent(
            uiState = uiState,
            onToggleSetDone = { workoutExerciseId, setNumber ->
                viewModel.toggleSetDone(workoutExerciseId, setNumber)
            },
            onFinishWorkoutClick = { viewModel.onFinishClick() }
        )

        if (uiState.showFinishDialog) {
            val totalExercises = uiState.exercises.size
            val completedExercises = uiState.exercises.count { it.sets.all { set -> set.isDone } }
            
            AlertDialog(
                onDismissRequest = { viewModel.onDismissFinishDialog() },
                title = { Text(stringResource(R.string.workout_finish_dialog_title)) },
                text = {
                    Text(
                        stringResource(
                            R.string.workout_finish_dialog_message,
                            completedExercises,
                            totalExercises
                        )
                    )
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.onConfirmFinish() }) {
                        Text(stringResource(R.string.workout_finish_dialog_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onDismissFinishDialog() }) {
                        Text(stringResource(R.string.workout_finish_dialog_cancel))
                    }
                }
            )
        }

        LaunchedEffect(uiState.workoutCompleted) {
            if (uiState.workoutCompleted) {
                onFinishWorkoutClick()
            }
        }
    }
}

@Composable
fun WorkoutStartContent(
    uiState: WorkoutStartUiState,
    onToggleSetDone: (Long, Int) -> Unit,
    onFinishWorkoutClick: () -> Unit
) {
    val totalSets = uiState.exercises.sumOf { it.sets.size }
    val doneSets = uiState.exercises.sumOf { it.sets.count { set -> set.isDone } }
    var expandedWorkoutExerciseIds by remember { mutableStateOf(setOf<Long>()) }

    LaunchedEffect(uiState.exercises) {
        if (expandedWorkoutExerciseIds.isEmpty() && uiState.exercises.isNotEmpty()) {
            expandedWorkoutExerciseIds = setOf(uiState.exercises.first().workoutExerciseId)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = uiState.workoutTitle,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.workout_start_status, doneSets, totalSets),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🕒", fontSize = 12.sp)
                        Text(
                            text = uiState.elapsedTime,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                LinearProgressIndicator(
                    progress = { if (totalSets > 0) doneSets.toFloat() / totalSets else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onFinishWorkoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.workout_start_finish_button),
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(uiState.exercises) { index, exercise ->
                ExerciseTrackingCard(
                    exercise = exercise,
                    index = index,
                    isExpanded = expandedWorkoutExerciseIds.contains(exercise.workoutExerciseId),
                    onExpandClick = {
                        expandedWorkoutExerciseIds = if (expandedWorkoutExerciseIds.contains(exercise.workoutExerciseId)) {
                            expandedWorkoutExerciseIds - exercise.workoutExerciseId
                        } else {
                            expandedWorkoutExerciseIds + exercise.workoutExerciseId
                        }
                    },
                    onToggleSetDone = onToggleSetDone
                )
            }
        }
    }
}

@Preview
@Composable
private fun WorkoutStartScreenPreview() {
    EvoFitTheme {
        WorkoutStartContent(
            uiState = WorkoutStartUiState(
                workoutTitle = "Peito",
                exercises = listOf(
                    ExerciseProgressState(
                        workoutExerciseId = 1L,
                        exerciseId = "1",
                        name = "Supino reto",
                        sets = listOf(
                            SetProgressState(1, 80.0, 10, true),
                            SetProgressState(2, 80.0, 10, false)
                        )
                    )
                ),
                isLoading = false
            ),
            onToggleSetDone = { _, _ -> },
            onFinishWorkoutClick = {}
        )
    }
}
