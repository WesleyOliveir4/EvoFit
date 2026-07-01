package com.example.evofit.presentation.ui.feature.workout.startworkout.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.workout.components.ExercisePreviewCard
import com.example.evofit.presentation.ui.feature.workout.components.ExercisePreviewItem
import com.example.evofit.presentation.ui.feature.workout.components.HeaderIndicatorCard
import com.example.evofit.presentation.ui.feature.workout.components.WorkoutDetailPreview
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutPreviewViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPreviewScreen(
    workoutId: Int,
    viewModel: WorkoutPreviewViewModel = koinViewModel(parameters = { parametersOf(workoutId) }),
    onBackClick: () -> Unit = {},
    onStartWorkoutClick: () -> Unit = {}
) {
    val previewState by viewModel.uiState.collectAsState()

    previewState?.let { preview ->
        WorkoutPreviewContent(
            preview = preview,
            onBackClick = onBackClick,
            onStartWorkoutClick = onStartWorkoutClick
        )
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPreviewContent(
    preview: WorkoutDetailPreview,
    onBackClick: () -> Unit,
    onStartWorkoutClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = preview.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onStartWorkoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⚡", fontSize = 16.sp)
                        Text(
                            text = stringResource(R.string.workout_preview_start_training),
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeaderIndicatorCard(
                        modifier = Modifier.weight(1f),
                        value = "${preview.totalExercises}",
                        label = stringResource(R.string.workout_preview_exercises)
                    )
                    
                    HeaderIndicatorCard(
                        modifier = Modifier.weight(1f),
                        value = "${preview.totalSets}",
                        label = stringResource(R.string.workout_preview_sets)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(preview.exercises) { index, exercise ->
                ExercisePreviewCard(index = index + 1, item = exercise)
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview
@Composable
private fun WorkoutPreviewScreenPreview() {
    EvoFitTheme {
        WorkoutPreviewContent(
            preview = WorkoutDetailPreview(
                title = "Peito",
                totalExercises = 3,
                totalSets = 7,
                exercises = listOf(
                    ExercisePreviewItem(1L, "Supino reto", 3, 80.0, 10),
                    ExercisePreviewItem(2L, "Supino inclinado", 2, 70.0, 10),
                    ExercisePreviewItem(3L, "Crucifixo", 2, 20.0, 12)
                )
            ),
            onBackClick = {},
            onStartWorkoutClick = {}
        )
    }
}
