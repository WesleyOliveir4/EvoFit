package com.guaracode.evofit.presentation.ui.feature.workout.startworkout.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.presentation.model.ExercisePreviewItem
import com.guaracode.evofit.presentation.model.WorkoutDetailPreview
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitAlertDialog
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitCautionDialog
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.workout.components.training.ExercisePreviewCard
import com.guaracode.evofit.presentation.ui.feature.workout.components.training.MuscleGroupPreviewCard
import com.guaracode.evofit.presentation.ui.feature.workout.components.training.rememberDraggableListState
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.components.HeaderIndicatorCard
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.tracking.StartWorkoutTracker
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutPreviewViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.workout_active_session_dialog_cancel
import evofit.shared.generated.resources.workout_active_session_dialog_confirm
import evofit.shared.generated.resources.workout_active_session_dialog_message
import evofit.shared.generated.resources.workout_active_session_dialog_title
import evofit.shared.generated.resources.workout_delete_blocked_dialog_confirm
import evofit.shared.generated.resources.workout_delete_blocked_dialog_message
import evofit.shared.generated.resources.workout_delete_blocked_dialog_title
import evofit.shared.generated.resources.workout_delete_dialog_cancel
import evofit.shared.generated.resources.workout_delete_dialog_confirm
import evofit.shared.generated.resources.workout_delete_dialog_message
import evofit.shared.generated.resources.workout_delete_dialog_title
import evofit.shared.generated.resources.workout_edit_blocked_dialog_confirm
import evofit.shared.generated.resources.workout_edit_blocked_dialog_message
import evofit.shared.generated.resources.workout_edit_blocked_dialog_title
import evofit.shared.generated.resources.workout_preview_delete_desc
import evofit.shared.generated.resources.workout_preview_edit_desc
import evofit.shared.generated.resources.workout_preview_exercises
import evofit.shared.generated.resources.workout_preview_sets
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPreviewScreen(
    workoutId: String,
    viewModel: WorkoutPreviewViewModel = koinViewModel(parameters = { parametersOf(workoutId) }),
    tracker: StartWorkoutTracker = koinInject(),
    onBackClick: () -> Unit = {},
    onStartWorkoutClick: () -> Unit = {},
    onEditClick: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(workoutId) {
        tracker.trackWorkoutPreviewScreenView(workoutId)
    }

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
                title = stringResource(Res.string.workout_active_session_dialog_title),
                description = stringResource(Res.string.workout_active_session_dialog_message),
                confirmButtonText = stringResource(Res.string.workout_active_session_dialog_confirm),
                dismissButtonText = stringResource(Res.string.workout_active_session_dialog_cancel),
                onConfirm = { viewModel.onConfirmDiscardActiveSession(onProceed = onStartWorkoutClick) },
                onDismiss = { viewModel.onDismissActiveSessionDialog() }
            )
        }

        if (uiState.showDeleteDialog) {
            EvoFitAlertDialog(
                title = stringResource(Res.string.workout_delete_dialog_title),
                description = stringResource(Res.string.workout_delete_dialog_message),
                icon = Icons.Default.Delete,
                confirmButtonText = stringResource(Res.string.workout_delete_dialog_confirm),
                dismissButtonText = stringResource(Res.string.workout_delete_dialog_cancel),
                onConfirm = { viewModel.onConfirmDelete() },
                onDismiss = { viewModel.onDismissDeleteDialog() }
            )
        }

        if (uiState.showEditBlockedDialog) {
            EvoFitAlertDialog(
                title = stringResource(Res.string.workout_edit_blocked_dialog_title),
                description = stringResource(Res.string.workout_edit_blocked_dialog_message),
                icon = Icons.Default.FrontHand,
                confirmButtonText = stringResource(Res.string.workout_edit_blocked_dialog_confirm),
                onConfirm = { viewModel.onDismissEditBlockedDialog() },
            )
        }

        if (uiState.showDeleteBlockedDialog) {
            EvoFitAlertDialog(
                title = stringResource(Res.string.workout_delete_blocked_dialog_title),
                description = stringResource(Res.string.workout_delete_blocked_dialog_message),
                icon = Icons.Default.FrontHand,
                confirmButtonText = stringResource(Res.string.workout_delete_blocked_dialog_confirm),
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
        onMove = { from, to ->
            val mutableList = localGroupedExercises.toMutableList()
            val movedItem = mutableList.removeAt(from)
            mutableList.add(to, movedItem)
            localGroupedExercises = mutableList
            onMove(mutableList.map { it.first })
        }
    )

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
                            contentDescription = stringResource(Res.string.workout_preview_edit_desc),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.workout_preview_delete_desc),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingMedium)
            ) {
                Button(
                    onClick = onStartWorkoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeightPrimary),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Iniciar Treino",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            item {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
                ) {
                    HeaderIndicatorCard(
                        value = preview.totalExercises.toString(),
                        label = stringResource(Res.string.workout_preview_exercises),
                        modifier = Modifier.weight(1f)
                    )
                    HeaderIndicatorCard(
                        value = preview.totalSets.toString(),
                        label = stringResource(Res.string.workout_preview_sets),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
            }

            localGroupedExercises.forEachIndexed { groupIndex, (groupName, exercises) ->
                val isExpanded = expandedGroups.contains(groupName)

                item(key = "group_$groupName") {
                    MuscleGroupPreviewCard(
                        groupName = groupName,
                        exercises = exercises,
                        isExpanded = isExpanded,
                        onExpandClick = {
                            expandedGroups = if (isExpanded) {
                                expandedGroups - groupName
                            } else {
                                expandedGroups + groupName
                            }
                        }
                    )
                }

                if (isExpanded) {
                    itemsIndexed(exercises, key = { _, item -> item.workoutExerciseId }) { exerciseIndex, exerciseItem ->
                        ExercisePreviewCard(
                            index = exerciseIndex + 1,
                            item = exerciseItem,
                            modifier = Modifier.padding(start = Dimens.SpacingMediumSmall)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.SpacingExtraExtraLarge))
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
                title = "Treino de Peito",
                muscleGroupId = "1",
                totalExercises = 4,
                totalSets = 16,
                exercises = emptyList(),
                groupedExercises = mapOf(
                    "Peitoral" to listOf(
                        ExercisePreviewItem(
                            workoutExerciseId = "1",
                            name = "Supino Reto",
                            setsCount = 4,
                            weight = 80.0,
                            reps = 10,
                            unit = MeasurementUnit.WEIGHT
                        )
                    )
                )
            ),
            onBackClick = {},
            onStartWorkoutClick = {}
        )
    }
}
