package com.guaracode.evofit.presentation.ui.feature.workout.home.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.navigation.NavRoutes
import com.guaracode.evofit.presentation.model.ActiveSessionUIModel
import com.guaracode.evofit.presentation.model.WorkoutHistoryUIModel
import com.guaracode.evofit.presentation.model.WorkoutUIModel
import com.guaracode.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.guaracode.evofit.presentation.ui.feature.workout.components.training.*
import com.guaracode.evofit.presentation.ui.feature.workout.home.tracking.WorkoutHomeTracker
import com.guaracode.evofit.presentation.ui.feature.workout.home.viewmodel.WorkoutViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_dumbbell
import evofit.shared.generated.resources.ic_fire
import evofit.shared.generated.resources.main_workout_active_session_title
import evofit.shared.generated.resources.main_workout_empty_history
import evofit.shared.generated.resources.main_workout_empty_workouts
import evofit.shared.generated.resources.main_workout_new_workout_desc
import evofit.shared.generated.resources.main_workout_stats_total_label
import evofit.shared.generated.resources.main_workout_stats_week_label
import evofit.shared.generated.resources.main_workout_tab_history
import evofit.shared.generated.resources.main_workout_tab_my_workouts
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel = koinViewModel(),
    tracker: WorkoutHomeTracker = koinInject(),
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        tracker.trackWorkoutHomeScreenView()
    }
    val isOnline by viewModel.isOnline.collectAsState()
    var showOfflineToast by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.showOfflineToast.collect { show ->
            if (show) {
                showOfflineToast = true
                delay(3000)
                showOfflineToast = false
            }
        }
    }

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
        activeSession = uiState.activeSession,
        isSyncing = uiState.isSyncing,
        isOnline = isOnline,
        showOfflineToast = showOfflineToast,
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
        onActiveSessionClick = { session ->
            onNavigate(NavRoutes.WorkoutStart.createRoute(session.workoutId))
        },
        onAddWorkoutClick = { onNavigate(NavRoutes.NewWorkout.createRoute()) }
    )
}

@Composable
fun WorkoutContent(
    userName: String,
    workouts: List<WorkoutUIModel>,
    totalWorkouts: Int,
    workoutsThisWeek: Int,
    history: List<WorkoutHistoryUIModel>,
    onMove: (Int, Int) -> Unit,
    onNavigate: (String) -> Unit,
    onWorkoutClick: (WorkoutUIModel) -> Unit,
    onAddWorkoutClick: () -> Unit,
    activeSession: ActiveSessionUIModel? = null,
    onActiveSessionClick: (ActiveSessionUIModel) -> Unit = {},
    isSyncing: Boolean = false,
    isOnline: Boolean = true,
    showOfflineToast: Boolean = false,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val dragState = rememberDraggableListState(onMove = onMove)
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddWorkoutClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(Dimens.SpacingMedium),
                modifier = Modifier.size(Dimens.FabSizeDefault)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.main_workout_new_workout_desc),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(Dimens.StatCardIconSize)
                )
            }
        },
        bottomBar = { 
            AppBottomNavigation(
                currentRoute = NavRoutes.Home.route,
                onNavigate = onNavigate
            ) 
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
            ) {
                item {
                    Spacer(modifier = Modifier.height(Dimens.SectionSpacing))
                    HeaderSection(
                        userName = userName,
                        isSyncing = isSyncing,
                        isOnline = isOnline
                    )
                    Spacer(modifier = Modifier.height(Dimens.SectionSpacing))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            value = totalWorkouts.toString(),
                            label = stringResource(Res.string.main_workout_stats_total_label),
                            icon = vectorResource(Res.drawable.ic_dumbbell)
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            value = workoutsThisWeek.toString(),
                            label = stringResource(Res.string.main_workout_stats_week_label),
                            icon = vectorResource(Res.drawable.ic_fire)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.SectionSpacing))
                }

                if (activeSession != null) {
                    item {
                        Text(
                            text = stringResource(Res.string.main_workout_active_session_title),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                        ActiveWorkoutCard(
                            workoutName = activeSession.workoutName,
                            onClick = { onActiveSessionClick(activeSession) }
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
                    }
                }

                item {
                    WorkoutSegmentedControl(
                        options = listOf(
                            stringResource(Res.string.main_workout_tab_my_workouts),
                            stringResource(Res.string.main_workout_tab_history)
                        ),
                        selectedIndex = selectedTabIndex,
                        onOptionSelected = { selectedTabIndex = it }
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                }

                if (selectedTabIndex == 0) {
                    if (workouts.isEmpty()) {
                        item {
                            WorkoutEmptyState(message = stringResource(Res.string.main_workout_empty_workouts))
                        }
                    } else {
                        draggableWorkoutList(
                            workouts = workouts,
                            dragState = dragState,
                            onWorkoutClick = onWorkoutClick
                        )
                    }
                } else {
                    if (history.isEmpty()) {
                        item {
                            WorkoutEmptyState(message = stringResource(Res.string.main_workout_empty_history))
                        }
                    } else {
                        items(history, key = { it.id }) { workoutDone ->
                            WorkoutDoneItem(
                                workoutDone = workoutDone,
                                modifier = Modifier.animateItem(
                                    fadeInSpec = null,
                                    fadeOutSpec = null
                                )
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(Dimens.TopAppBarHeightSmall + Dimens.SectionSpacing)) }
            }

            AnimatedVisibility(
                visible = showOfflineToast,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                OfflineToast()
            }
        }
    }
}

@Preview
@Composable
private fun WorkoutContentPreview() {
    EvoFitTheme {
        WorkoutContent(
            userName = "Augusto",
            workouts = listOf(
                WorkoutUIModel("1", "Peito", 6, 20),
                WorkoutUIModel("2", "Costas", 6, 18),
                WorkoutUIModel("3", "Pernas", 8, 24)
            ),
            totalWorkouts = 124,
            workoutsThisWeek = 3,
            history = emptyList(),
            onMove = { _, _ -> },
            onNavigate = {},
            onWorkoutClick = {},
            onAddWorkoutClick = {},
            isSyncing = true,
            isOnline = false
        )
    }
}
