package com.guaracode.evofit.presentation.ui.feature.workout.startworkout.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.guaracode.evofit.presentation.mapper.ExerciseMapper
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitActionDialog
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitCautionDialog
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.components.ExerciseTrackingCard
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.session.ExerciseProgressState
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.session.SetProgressState
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.session.WorkoutStartUiState
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutStartViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.tracking.StartWorkoutTracker
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_logo_evofit
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun WorkoutStartScreen(
    workoutId: String,
    viewModel: WorkoutStartViewModel = koinViewModel(parameters = { parametersOf(workoutId) }),
    tracker: StartWorkoutTracker = koinInject(),
    onFinishWorkoutClick: (String?) -> Unit = {},
    onCancelWorkoutClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showImageForExerciseId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(workoutId) {
        tracker.trackWorkoutStartScreenView(workoutId)
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            WorkoutStartContent(
                uiState = uiState,
                onToggleSetDone = { workoutExerciseId, setNumber ->
                    viewModel.toggleSetDone(workoutExerciseId, setNumber)
                },
                onFinishWorkoutClick = { viewModel.onFinishClick() },
                onCancelWorkoutClick = { viewModel.onCancelWorkoutClick() },
                onBackClick = onBackClick,
                onInfoClick = { showImageForExerciseId = it }
            )

            showImageForExerciseId?.let { exerciseId ->
                Dialog(
                    onDismissRequest = { showImageForExerciseId = null },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f))
                            .clickable(
                                onClick = { showImageForExerciseId = null },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(ExerciseMapper.toImageRes(exerciseId)),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .offset(y = (-50).dp)
                                .clip(RoundedCornerShape(Dimens.CornerRadiusLarge))
                                .clickable(enabled = false) {},
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
        }

        if (uiState.showFinishDialog) {
            val totalExercises = uiState.exercises.size
            val completedExercises = uiState.exercises.count { it.sets.all { set -> set.isDone } }
            EvoFitActionDialog(
                title = "Finalizar treino?",
                description = "Você completou $completedExercises de $totalExercises exercícios.",
                confirmButtonText = "Finalizar",
                dismissButtonText = "Cancelar",
                onDismiss = { viewModel.onDismissFinishDialog() },
                onConfirm = { viewModel.onConfirmFinish() }
            )
        }

        if (uiState.showCancelDialog) {
            EvoFitCautionDialog(
                title = "Sair sem salvar?",
                description = "Seu progresso atual na ficha de treino não será registrado. Deseja realmente sair?",
                confirmButtonText = "Sair",
                dismissButtonText = "Continuar treinando",
                onDismiss = { viewModel.onDismissCancelDialog() },
                onConfirm = { viewModel.onConfirmCancelWorkout() }
            )
        }

        LaunchedEffect(uiState.workoutCompleted, uiState.workoutNotFinished) {
            if (uiState.workoutCompleted) {
                onFinishWorkoutClick(uiState.workoutDoneId)
            } else if (uiState.workoutNotFinished) {
                onCancelWorkoutClick(workoutId)
            }
        }
    }
}

@Composable
fun WorkoutStartContent(
    uiState: WorkoutStartUiState,
    onToggleSetDone: (String, Int) -> Unit,
    onFinishWorkoutClick: () -> Unit,
    onCancelWorkoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onInfoClick: (String) -> Unit = {}
) {
    val totalSets = uiState.exercises.sumOf { it.sets.size }
    val doneSets = uiState.exercises.sumOf { it.sets.count { set -> set.isDone } }
    var expandedWorkoutExerciseIds by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(uiState.exercises) {
        if (expandedWorkoutExerciseIds.isEmpty() && uiState.exercises.isNotEmpty()) {
            expandedWorkoutExerciseIds = setOf(uiState.exercises.first().workoutExerciseId)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingSmall)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.size(Dimens.MinimumTouchTarget)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = onCancelWorkoutClick, modifier = Modifier.size(Dimens.MinimumTouchTarget)) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_logo_evofit),
                            contentDescription = "Cancelar treino",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.SpacingExtraSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = uiState.workoutTitle,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingExtraExtraSmall))
                        Text(
                            text = "$doneSets de $totalSets séries concluídas",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(Dimens.CornerRadiusDefault))
                            .padding(horizontal = Dimens.SpacingMediumSmall, vertical = Dimens.SpacingTiny),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timelapse,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Dimens.IconSizeSmall)
                        )
                        Text(
                            text = uiState.elapsedTime,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.SpacingMediumSmall))
                
                LinearProgressIndicator(
                    progress = { if (totalSets > 0) doneSets.toFloat() / totalSets else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.SpacingExtraSmall)
                        .clip(RoundedCornerShape(Dimens.SpacingExtraExtraSmall)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingMedium)
            ) {
                Button(
                    onClick = onFinishWorkoutClick,
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
                        text = "Concluir treino",
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
            val groupedExercises = uiState.exercises.groupBy { it.muscleGroupName }
            
            groupedExercises.forEach { (groupName, exercises) ->
                item {
                    Text(
                        text = groupName.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = Dimens.SpacingSmall, bottom = Dimens.SpacingExtraSmall)
                    )
                }

                itemsIndexed(
                    items = exercises,
                    key = { _, exercise -> exercise.workoutExerciseId }
                ) { index, exercise ->
                    ExerciseTrackingCard(
                        exercise = exercise,
                        index = index,
                        isExpanded = expandedWorkoutExerciseIds.contains(exercise.workoutExerciseId),
                        onExpandClick = {
                            expandedWorkoutExerciseIds = if (expandedWorkoutExerciseIds.contains(exercise.workoutExerciseId)) {
                                expandedWorkoutExerciseIds - exercise.workoutExerciseId
                            } else {
                                expandedWorkoutExerciseIds + exercise.workoutExerciseId
                            }
                        },
                        onToggleSetDone = onToggleSetDone,
                        onInfoClick = onInfoClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WorkoutStartScreenPreview() {
    EvoFitTheme {
        WorkoutStartContent(
            uiState = WorkoutStartUiState(
                workoutTitle = "Peito",
                exercises = listOf(
                    ExerciseProgressState(
                        workoutExerciseId = "1",
                        exerciseId = "1",
                        name = "Supino reto",
                        sets = listOf(
                            SetProgressState(
                                setNumber = 1,
                                weight = 80.0,
                                reps = 10,
                                isDone = true
                            )
                        )
                    )
                ),
                isLoading = false
            ),
            onToggleSetDone = { _, _ -> },
            onFinishWorkoutClick = {},
            onInfoClick = {}
        )
    }
}
