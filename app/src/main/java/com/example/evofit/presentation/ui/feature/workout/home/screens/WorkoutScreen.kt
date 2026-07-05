package com.example.evofit.presentation.ui.feature.workout.home.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.navigation.NavRoutes
import com.example.evofit.presentation.model.WorkoutUIModel
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.workout.components.training.ExercisePreviewCard
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.ui.feature.workout.components.training.HeaderSection
import com.example.evofit.presentation.ui.feature.workout.components.training.StatCard
import com.example.evofit.presentation.ui.feature.workout.components.training.draggableWorkoutList
import com.example.evofit.presentation.ui.feature.workout.components.training.rememberWorkoutDraggableListState
import com.example.evofit.presentation.ui.feature.workout.home.viewmodel.WorkoutViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel = koinViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var localWorkouts by remember { mutableStateOf<List<WorkoutUIModel>>(emptyList()) }
    
    LaunchedEffect(uiState.workouts) {
        localWorkouts = uiState.workouts
    }

    WorkoutContent(
        userName = uiState.userName,
        workouts = localWorkouts,
        totalWorkouts = uiState.totalWorkouts,
        workoutsThisWeek = uiState.workoutsThisWeek,
        history = uiState.history,
        onMove = { from, to ->
            val mutableList = localWorkouts.toMutableList()
            mutableList.add(to, mutableList.removeAt(from))
            localWorkouts = mutableList
            viewModel.updateWorkoutOrder(localWorkouts)
        },
        onNavigate = onNavigate,
        onWorkoutClick = { workout ->
            onNavigate(NavRoutes.WorkoutPreview.createRoute(workout.id))
        },
        onAddWorkoutClick = { onNavigate(NavRoutes.NewWorkout.route) }
    )
}

@Composable
fun WorkoutContent(
    userName: String,
    workouts: List<WorkoutUIModel>,
    totalWorkouts: Int,
    workoutsThisWeek: Int,
    history: List<WorkoutDone>,
    onMove: (Int, Int) -> Unit,
    onNavigate: (String) -> Unit,
    onWorkoutClick: (WorkoutUIModel) -> Unit,
    onAddWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val dragState = rememberWorkoutDraggableListState(onMove = onMove)

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddWorkoutClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.main_workout_new_workout_desc),
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        bottomBar = { 
            AppBottomNavigation(
                currentRoute = "home",
                onNavigate = onNavigate
            ) 
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                HeaderSection(userName = userName)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = totalWorkouts.toString(),
                        label = stringResource(R.string.main_workout_stats_total_label),
                        icon = Icons.Default.SettingsInputComponent
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = workoutsThisWeek.toString(),
                        label = stringResource(R.string.main_workout_stats_week_label),
                        icon = Icons.Default.Whatshot
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.main_workout_section_title),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            draggableWorkoutList(
                workouts = workouts,
                dragState = dragState,
                onWorkoutClick = onWorkoutClick
            )

            if (history.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Histórico Recente",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(history.takeLast(5).reversed()) { workoutDone ->
                    WorkoutDoneItem(workoutDone = workoutDone)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun WorkoutDoneItem(
    workoutDone: WorkoutDone,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

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
        Column(modifier = Modifier.padding(16.dp)) {
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
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                workoutDone.exercises.forEachIndexed { index, workoutExercise ->
                    val sets = workoutExercise.sets
                    val firstSet = sets.firstOrNull()
                    ExercisePreviewCard(
                        index = index + 1,
                        item = ExercisePreviewItem(
                            workoutExerciseId = workoutExercise.id,
                            name = firstSet?.exerciseName ?: "",
                            setsCount = sets.size,
                            weight = sets.maxOfOrNull { it.load } ?: 0.0,
                            reps = sets.maxOfOrNull { it.reps } ?: 0,
                            unit = firstSet?.unit ?: MeasurementUnit.WEIGHT,
                            time = sets.maxOfOrNull { it.time ?: 0 } ?: 0,
                            distance = sets.maxOfOrNull { it.distance ?: 0.0 } ?: 0.0
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutContentPreview() {
    EvoFitTheme {
        WorkoutContent(
            userName = "User",
            workouts = listOf(
                WorkoutUIModel(1, "Peito", 3, 7, Icons.Default.Favorite),
                WorkoutUIModel(2, "Costas", 2, 4, Icons.Default.ArrowBack)
            ),
            totalWorkouts = 2,
            workoutsThisWeek = 1,
            history = emptyList(),
            onMove = { _, _ -> },
            onNavigate = {},
            onWorkoutClick = {},
            onAddWorkoutClick = {}
        )
    }
}
