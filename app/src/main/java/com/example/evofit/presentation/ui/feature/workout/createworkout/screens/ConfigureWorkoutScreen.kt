package com.example.evofit.presentation.ui.feature.workout.createworkout.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.ui.feature.workout.components.ExercisePageSegmentedIndicator
import com.example.evofit.presentation.ui.feature.workout.createworkout.components.ExerciseConfigContent
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.ConfigureWorkoutViewModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.ExerciseConfigState
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SetState
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigureWorkoutScreen(
    exerciseIds: List<String>,
    workoutName: String,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
    viewModel: ConfigureWorkoutViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(exerciseIds, workoutName) {
        viewModel.loadExercises(exerciseIds, workoutName)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onFinishClick()
        }
    }

    if (uiState.exerciseConfigs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { uiState.exerciseConfigs.size })

    BackHandler {
        if (pagerState.currentPage > 0) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        } else {
            onBackClick()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            R.string.configure_workout_title,
                            pagerState.currentPage + 1,
                            uiState.exerciseConfigs.size
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (pagerState.currentPage > 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.new_workout_back_desc),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
                    onClick = {
                        if (pagerState.currentPage < uiState.exerciseConfigs.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            viewModel.saveWorkout()
                        }
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black
                        )
                    } else {
                        Text(
                            text = if (pagerState.currentPage == uiState.exerciseConfigs.size - 1) {
                                stringResource(R.string.configure_workout_finish)
                            } else {
                                stringResource(R.string.configure_workout_next)
                            },
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ExercisePageSegmentedIndicator(
                totalCount = uiState.exerciseConfigs.size,
                currentIndex = pagerState.currentPage
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = !uiState.isLoading
            ) { page ->
                ExerciseConfigContent(
                    config = uiState.exerciseConfigs[page],
                    muscleGroupType = uiState.muscleGroupType,
                    onAddSet = { viewModel.addSet(it) },
                    onUpdateSet = { id, idx, weight, reps -> viewModel.updateSet(id, idx, weight, reps) },
                    onRemoveSet = { id, idx -> viewModel.removeSet(id, idx) }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ConfigureWorkoutScreenPreview() {
    EvoFitTheme {
        val mockConfig = remember {
            ExerciseConfigState(
                exerciseId = "1",
                name = "Supino Reto",
                muscleGroupId = "2",
                unit = MeasurementUnit.WEIGHT,
                sets = listOf(
                    SetState(1, 30.0, 12),
                    SetState(2, 30.0, 12),
                    SetState(3, 25.0, 15)
                )
            )
        }
        
        Scaffold { padding ->
            Column(modifier = Modifier.padding(padding)) {
                ExercisePageSegmentedIndicator(totalCount = 3, currentIndex = 0)
                ExerciseConfigContent(
                    config = mockConfig,
                    muscleGroupType = MuscleGroupType.CHEST,
                    onAddSet = {},
                    onUpdateSet = { _, _, _, _ -> },
                    onRemoveSet = { _, _ -> }
                )
            }
        }
    }
}

