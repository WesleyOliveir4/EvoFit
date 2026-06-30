package com.example.evofit.presentation.ui.feature.workout.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.workout.components.AddSetDashedButton
import com.example.evofit.presentation.ui.feature.workout.components.ExercisePageSegmentedIndicator
import com.example.evofit.presentation.ui.feature.workout.components.RepsCounterComponent
import com.example.evofit.presentation.ui.feature.workout.components.WeightWheelSelector
import com.example.evofit.presentation.ui.feature.workout.viewmodel.ConfigureWorkoutViewModel
import com.example.evofit.presentation.ui.feature.workout.viewmodel.ExerciseConfigState
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.presentation.ui.feature.workout.viewmodel.SetState
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigureWorkoutScreen(
    exerciseIds: List<String>,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit,
    viewModel: ConfigureWorkoutViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val configs = viewModel.exerciseConfigs
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(exerciseIds) {
        viewModel.loadExercises(exerciseIds)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onFinishClick()
        }
    }

    if (configs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { configs.size })

    // Intercepta o botão voltar (gesto/físico)
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
                            configs.size
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
                            contentDescription = "Voltar",
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
                        if (pagerState.currentPage < configs.size - 1) {
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
                            text = if (pagerState.currentPage == configs.size - 1) {
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
                totalCount = configs.size,
                currentIndex = pagerState.currentPage
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = !uiState.isLoading
            ) { page ->
                ExerciseConfigContent(
                    config = configs[page],
                    onAddSet = { viewModel.addSet(it) },
                    onUpdateSet = { id, idx, weight, reps -> viewModel.updateSet(id, idx, weight, reps) }
                )
            }
        }
    }
}

@Composable
fun ExerciseConfigContent(
    config: ExerciseConfigState,
    onAddSet: (String) -> Unit,
    onUpdateSet: (String, Int, Double, Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏋️", fontSize = 20.sp)
                }
                Column {
                    Text(
                        text = config.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(R.string.configure_workout_header_sets, config.sets.size),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.configure_workout_col_set),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.configure_workout_col_weight),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.configure_workout_col_reps),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1.2f),
                    textAlign = TextAlign.Center
                )
            }
        }

        itemsIndexed(config.sets) { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicador de Série
                Box(
                    modifier = Modifier
                        .weight(0.8f)
                        .height(48.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                WeightWheelSelector(
                    modifier = Modifier.weight(1f),
                    initialWeight = item.weight,
                    onWeightSelected = { newWeight ->
                        onUpdateSet(config.exerciseId, index, newWeight, item.reps)
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                RepsCounterComponent(
                    modifier = Modifier.weight(1.2f),
                    value = item.reps,
                    onValueChange = { newReps ->
                        onUpdateSet(config.exerciseId, index, item.weight, newReps)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            AddSetDashedButton(
                onClick = { onAddSet(config.exerciseId) }
            )
            Spacer(modifier = Modifier.height(40.dp))
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
                sets = mutableStateListOf(
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
                    onAddSet = {},
                    onUpdateSet = { _, _, _, _ -> }
                )
            }
        }
    }
}

