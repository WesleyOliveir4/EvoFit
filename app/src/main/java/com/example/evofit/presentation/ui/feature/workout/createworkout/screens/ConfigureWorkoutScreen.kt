package com.example.evofit.presentation.ui.feature.workout.createworkout.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.presentation.ui.feature.workout.components.configure.ExercisePageSegmentedIndicator
import com.example.evofit.presentation.ui.feature.workout.createworkout.components.ExerciseConfigContent
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.ExerciseConfigState
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.SetState
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.ConfigureWorkoutViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigureWorkoutScreen(
    exerciseIds: List<String>,
    workoutName: String,
    onBackClick: () -> Unit,
    onFinishClick: (String) -> Unit,
    editWorkoutId: String? = null,
    onFinishEditClick: (String) -> Unit = {},
    viewModel: ConfigureWorkoutViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(exerciseIds, editWorkoutId) {
        viewModel.loadExercises(exerciseIds, editWorkoutId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            val savedWorkoutId = uiState.savedWorkoutId
            val editWorkoutId = uiState.editWorkoutId
            if (editWorkoutId != null) {
                onFinishEditClick(editWorkoutId)
            } else if (savedWorkoutId != null) {
                onFinishClick(savedWorkoutId)
            } else {
                //Adicionar Tela de Erro
                onFinishClick("Error")
            }
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
                            if (uiState.editWorkoutId != null) {
                                viewModel.saveEditedWorkout(workoutName)
                            } else {
                                viewModel.saveWorkout(workoutName)
                            }
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

