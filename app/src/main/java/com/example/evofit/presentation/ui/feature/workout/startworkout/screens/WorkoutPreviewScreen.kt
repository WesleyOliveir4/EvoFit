package com.example.evofit.presentation.ui.feature.workout.startworkout.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.model.WorkoutDetailPreview
import com.example.evofit.presentation.ui.feature.workout.components.training.ExercisePreviewCard
import com.example.evofit.presentation.ui.feature.workout.startworkout.components.HeaderIndicatorCard
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutPreviewViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.evofit.presentation.ui.feature.components.EvoFitAlertDialog
import com.example.evofit.presentation.ui.feature.components.EvoFitCautionDialog
import com.example.evofit.presentation.ui.feature.workout.components.training.MuscleGroupPreviewCard
import com.example.evofit.presentation.ui.feature.workout.components.training.rememberDraggableListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPreviewScreen(
    workoutId: String,
    viewModel: WorkoutPreviewViewModel = koinViewModel(parameters = { parametersOf(workoutId) }),
    onBackClick: () -> Unit = {},
    onStartWorkoutClick: () -> Unit = {},
    onEditClick: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onBackClick()
        }
    }

    uiState.preview?.let { preview ->
        WorkoutPreviewContent(
            preview = preview,
            onBackClick = onBackClick,
            onStartWorkoutClick = { viewModel.onStartWorkoutClicked(onProceed = onStartWorkoutClick) },
            onEditClick = {
                viewModel.onEditClicked(onProceed = { onEditClick(preview.muscleGroupId, workoutId) })
            },
            onDeleteClick = { viewModel.onDeleteClicked() },
            onMove = { newOrder -> viewModel.updateMuscleGroupOrder(newOrder) }
        )

        if (uiState.hasActiveSessionConflict) {
            EvoFitCautionDialog(
                title = stringResource(R.string.workout_active_session_dialog_title),
                description = stringResource(R.string.workout_active_session_dialog_message),
                confirmButtonText = stringResource(R.string.workout_active_session_dialog_confirm),
                dismissButtonText = stringResource(R.string.workout_active_session_dialog_cancel),
                onConfirm = { viewModel.onConfirmDiscardActiveSession(onProceed = onStartWorkoutClick) },
                onDismiss = { viewModel.onDismissActiveSessionDialog() }
            )
        }

        if (uiState.showDeleteDialog) {
            EvoFitAlertDialog(
                title = stringResource(R.string.workout_delete_dialog_title),
                description = stringResource(R.string.workout_delete_dialog_message),
                icon = Icons.Default.Delete,
                confirmButtonText = stringResource(R.string.workout_delete_dialog_confirm),
                dismissButtonText = stringResource(R.string.workout_delete_dialog_cancel),
                onConfirm = { viewModel.onConfirmDelete() },
                onDismiss = { viewModel.onDismissDeleteDialog() }
            )
        }

        if (uiState.showEditBlockedDialog) {
            EvoFitAlertDialog(
                title = stringResource(R.string.workout_edit_blocked_dialog_title),
                description = stringResource(R.string.workout_edit_blocked_dialog_message),
                icon = Icons.Default.FrontHand,
                confirmButtonText = stringResource(R.string.workout_edit_blocked_dialog_confirm),
                onConfirm = { viewModel.onDismissEditBlockedDialog() },
            )
        }

        if (uiState.showDeleteBlockedDialog) {
            EvoFitAlertDialog(
                title = stringResource(R.string.workout_delete_blocked_dialog_title),
                description = stringResource(R.string.workout_delete_blocked_dialog_message),
                icon = Icons.Default.FrontHand,
                confirmButtonText = stringResource(R.string.workout_delete_blocked_dialog_confirm),
                onConfirm = { viewModel.onDismissDeleteBlockedDialog() },
            )
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPreviewContent(
    preview: WorkoutDetailPreview,
    onBackClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onMove: (List<String>) -> Unit = {}
) {
    var expandedGroups by remember { mutableStateOf(setOf<String>()) }

    var localGroupedExercises by remember { mutableStateOf<List<Pair<String, List<ExercisePreviewItem>>>>(emptyList()) }

    LaunchedEffect(preview.groupedExercises) {
        localGroupedExercises = preview.groupedExercises.toList()
    }

    val dragState = rememberDraggableListState(
        itemHeight = 80.dp,
        onMove = { from, to ->
            val mutableList = localGroupedExercises.toMutableList()
            mutableList.add(to, mutableList.removeAt(from))
            localGroupedExercises = mutableList
            onMove(localGroupedExercises.map { it.first })
        }
    )

    LaunchedEffect(preview.groupedExercises) {
        if (expandedGroups.isEmpty() && preview.groupedExercises.isNotEmpty()) {
            expandedGroups = setOf(preview.groupedExercises.keys.first())
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarReturn(
                title = preview.title,
                onBackClick = onBackClick,
                isCenterAligned = false,
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.workout_preview_edit_desc),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.workout_preview_delete_desc),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(Dimens.ScreenPaddingHorizontal)
            ) {
                Button(
                    onClick = onStartWorkoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeightPrimary),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                    ) {
                        Text(
                            text = stringResource(R.string.workout_preview_start_training),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
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
                .padding(horizontal = Dimens.ScreenPaddingHorizontal),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            item {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
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
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
            }

            itemsIndexed(localGroupedExercises, key = { _, pair -> pair.first }) { _, (groupName, exercises) ->
                val isDragging = dragState.draggedItemId == groupName
                MuscleGroupPreviewCard(
                    groupName = groupName,
                    exercises = exercises,
                    isExpanded = expandedGroups.contains(groupName) && !isDragging,
                    onExpandClick = {
                        expandedGroups = if (expandedGroups.contains(groupName)) {
                            expandedGroups - groupName
                        } else {
                            expandedGroups + groupName
                        }
                    },
                    isDragging = isDragging,
                    dragOffset = { if (isDragging) dragState.dragOffset else 0f },
                    onDragStart = { dragState.onDragStart(groupName) },
                    onDrag = { deltaY -> dragState.onDrag(deltaY, localGroupedExercises, { it.first }) },
                    onDragEnd = dragState::onDragEnd,
                    onDragCancel = dragState::onDragEnd,
                    modifier = Modifier.animateItem(
                        fadeInSpec = null,
                        fadeOutSpec = null
                    )
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
            }
        }
    }
}

@Preview
@Composable
private fun WorkoutPreviewScreenPreview() {
    val exercises = listOf(
        ExercisePreviewItem(
            workoutExerciseId = "1",
            name = "Supino reto",
            muscleGroupName = "Peito",
            setsCount = 3,
            weight = 80.0,
            reps = 10,
            unit = MeasurementUnit.WEIGHT
        ),
        ExercisePreviewItem(
            workoutExerciseId = "2",
            name = "Corrida",
            muscleGroupName = "Cardio",
            setsCount = 1,
            weight = 0.0,
            reps = 0,
            unit = MeasurementUnit.DISTANCE,
            distance = 5.0,
            time = 25
        ),
        ExercisePreviewItem(
            workoutExerciseId = "3",
            name = "Prancha",
            muscleGroupName = "Core",
            setsCount = 3,
            weight = 0.0,
            reps = 0,
            unit = MeasurementUnit.TIME,
            time = 2
        ),
        ExercisePreviewItem(
            workoutExerciseId = "4",
            name = "Abdominais",
            muscleGroupName = "Core",
            setsCount = 3,
            weight = 0.0,
            reps = 20,
            unit = MeasurementUnit.REPS
        )
    )

    EvoFitTheme {
        WorkoutPreviewContent(
            preview = WorkoutDetailPreview(
                title = "Treino Completo",
                muscleGroupId = "chest",
                totalExercises = 4,
                totalSets = 10,
                exercises = exercises,
                groupedExercises = exercises.groupBy { it.muscleGroupName }
            ),
            onBackClick = {},
            onStartWorkoutClick = {}
        )
    }
}
