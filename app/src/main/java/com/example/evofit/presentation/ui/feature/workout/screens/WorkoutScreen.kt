package com.example.evofit.presentation.ui.feature.workout.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.navigation.NavRoutes
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.workout.components.HeaderSection
import com.example.evofit.presentation.ui.feature.workout.components.StatCard
import com.example.evofit.presentation.ui.feature.workout.components.WorkoutUIModel
import com.example.evofit.presentation.ui.feature.workout.components.draggableWorkoutList
import com.example.evofit.presentation.ui.feature.workout.components.rememberWorkoutDraggableListState
import com.example.evofit.presentation.ui.feature.workout.viewmodel.WorkoutViewModel
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
        onMove = { from, to ->
            val mutableList = localWorkouts.toMutableList()
            mutableList.add(to, mutableList.removeAt(from))
            localWorkouts = mutableList
        },
        onNavigate = onNavigate,
        onAddWorkoutClick = { onNavigate(NavRoutes.NewWorkout.route) }
    )
}

@Composable
fun WorkoutContent(
    userName: String,
    workouts: List<WorkoutUIModel>,
    totalWorkouts: Int,
    workoutsThisWeek: Int,
    onMove: (Int, Int) -> Unit,
    onNavigate: (String) -> Unit,
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

            draggableWorkoutList(workouts, dragState)
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
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
            onMove = { _, _ -> },
            onNavigate = {},
            onAddWorkoutClick = {}
        )
    }
}
