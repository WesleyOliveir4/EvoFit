package com.guaracode.evofit.presentation.ui.feature.workout.startworkout.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.presentation.model.ExercisePreviewItem
import com.guaracode.evofit.presentation.model.WorkoutDetailPreview
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.components.HeaderIndicatorCard
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutPreviewViewModel
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitAlertDialog
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitCautionDialog
import com.guaracode.evofit.presentation.ui.feature.workout.components.training.MuscleGroupPreviewCard
import com.guaracode.evofit.presentation.ui.feature.workout.components.training.rememberDraggableListState
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.tracking.StartWorkoutTracker
import org.koin.compose.koinInject

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
                title = "Sessão Ativa Conflitante",
                description = "Existe um treino em andamento. Deseja descartar a sessão atual e iniciar este treino?",
                confirmButtonText = "Descartar e Iniciar",
                dismissButtonText = "Cancelar",
                onConfirm = { viewModel.onConfirmDiscardActiveSession(onProceed = onStartWorkoutClick) },
                onDismiss = { viewModel.onDismissActiveSessionDialog() }
            )
        }

        if (uiState.showDeleteDialog) {
            EvoFitAlertDialog(
                title = "Excluir Treino?",
                description = "Esta ação apagar o treino e não pode ser desfeita.",
                confirmButtonText = "Excluir",
                dismissButtonText = "Cancelar",
                onConfirm = { viewModel.onConfirmDelete() },
                onDismiss = { viewModel.onDismissDeleteDialog() }
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
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Excluir",
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
                            text = "Iniciar Treino",
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
                        label = "Exercícios"
                    )
                    HeaderIndicatorCard(
                        modifier = Modifier.weight(1f),
                        value = "${preview.totalSets}",
                        label = "Séries"
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
            workoutExerciseId = "11",
            name = "Supino Reto",
            muscleGroupName = "Peito",
            setsCount = 3,
            weight = 80.0,
            reps = 10,
            unit = MeasurementUnit.WEIGHT
        )
    )

    EvoFitTheme {
        WorkoutPreviewContent(
            preview = WorkoutDetailPreview(
                title = "Treino A",
                muscleGroupId = "2",
                totalExercises = 1,
                totalSets = 3,
                exercises = exercises,
                groupedExercises = exercises.groupBy { it.muscleGroupName }
            ),
            onBackClick = {},
            onStartWorkoutClick = {}
        )
    }
}
