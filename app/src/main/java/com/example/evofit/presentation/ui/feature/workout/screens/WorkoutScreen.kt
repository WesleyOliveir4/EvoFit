package com.example.evofit.presentation.ui.feature.workout.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.home.viewmodel.HomeViewModel
import com.example.evofit.presentation.ui.feature.workout.components.HeaderSection
import com.example.evofit.presentation.ui.feature.workout.components.StatCard
import com.example.evofit.presentation.ui.feature.workout.components.WorkoutListItem
import com.example.evofit.presentation.ui.feature.workout.components.WorkoutUIModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val userData by viewModel.userData.collectAsState()
    
    val workouts = remember {
        mutableStateListOf(
            WorkoutUIModel(1, "Peito", 3, 7, Icons.Default.Favorite),
            WorkoutUIModel(2, "Costas", 2, 4, Icons.Default.ArrowBack),
            WorkoutUIModel(3, "Pernas", 2, 5, Icons.Default.DirectionsRun),
            WorkoutUIModel(4, "Ombros", 1, 2, Icons.Default.Face),
            WorkoutUIModel(5, "Braços", 2, 4, Icons.Default.ThumbUp)
        )
    }

    WorkoutContent(
        userName = userData.name,
        workouts = workouts,
        onMove = { from, to ->
            workouts.add(to, workouts.removeAt(from))
        },
        onNavigate = onNavigate,
        onAddWorkoutClick = { /* Adicionar novo treino */ }
    )
}

@Composable
fun WorkoutContent(
    userName: String,
    workouts: List<WorkoutUIModel>,
    onMove: (Int, Int) -> Unit,
    onNavigate: (String) -> Unit,
    onAddWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var draggedItemId by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

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
                        value = "5",
                        label = stringResource(R.string.main_workout_stats_total_label),
                        icon = Icons.Default.SettingsInputComponent
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = "3",
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

            itemsIndexed(workouts, key = { _, it -> it.id }) { index, workout ->
                val density = LocalDensity.current
                val isDragging = draggedItemId == workout.id
                
                WorkoutListItem(
                    workout = workout,
                    modifier = Modifier
                        .zIndex(if (isDragging) 10f else 1f)
                        .then(if (isDragging) Modifier else Modifier.animateItem())
                        .then(
                            if (isDragging) {
                                Modifier
                                    .offset(y = with(density) { dragOffset.toDp() })
                                    .scale(1.05f)
                                    .shadow(12.dp, RoundedCornerShape(16.dp))
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            } else Modifier
                        ),
                    handleModifier = Modifier.pointerInput(workout.id) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { 
                                draggedItemId = workout.id
                                dragOffset = 0f 
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount.y
                                
                                val itemHeightPx = with(density) { 104.dp.toPx() }
                                val currentIndex = workouts.indexOfFirst { it.id == draggedItemId }
                                
                                if (currentIndex != -1) {
                                    while (dragOffset > itemHeightPx * 0.5f && currentIndex < workouts.size - 1) {
                                        onMove(currentIndex, currentIndex + 1)
                                        dragOffset -= itemHeightPx
                                        break
                                    }
                                    while (dragOffset < -itemHeightPx * 0.5f && currentIndex > 0) {
                                        onMove(currentIndex, currentIndex - 1)
                                        dragOffset += itemHeightPx
                                        break
                                    }
                                }
                            },
                            onDragEnd = {
                                draggedItemId = null
                                dragOffset = 0f
                            },
                            onDragCancel = {
                                draggedItemId = null
                                dragOffset = 0f
                            }
                        )
                    }
                )
            }
            
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
            onMove = { _, _ -> },
            onNavigate = {},
            onAddWorkoutClick = {}
        )
    }
}
