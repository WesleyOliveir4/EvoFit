package com.guaracode.evofit.presentation.ui.feature.workout.createworkout.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
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
import com.guaracode.evofit.presentation.model.ExerciseSelectionUIModel
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.workout.components.configure.ExerciseRowItem
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.tracking.CreateWorkoutTracker
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SelectExercisesViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SelectExercisesScreen(
    muscleGroupIds: List<String>,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onConfigureExercisesClick: (List<String>, String, String?) -> Unit,
    editWorkoutId: String? = null,
    viewModel: SelectExercisesViewModel = koinViewModel(),
    tracker: CreateWorkoutTracker = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentGroupId = uiState.muscleGroupIds.getOrNull(uiState.currentGroupIndex) ?: ""
    val selectedIdsForCurrentGroup = uiState.allSelectedExerciseIds[currentGroupId] ?: emptySet()
    
    var showImageForExerciseId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        tracker.trackSelectExercisesScreenView()
    }

    LaunchedEffect(muscleGroupIds, editWorkoutId) {
        viewModel.loadInitialData(muscleGroupIds, editWorkoutId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SelectExercisesContent(
            muscleGroupName = uiState.muscleGroupName,
            workoutName = uiState.workoutName,
            tempWorkoutName = uiState.tempWorkoutName,
            isEditingName = uiState.isEditingName,
            isEditMode = uiState.editWorkoutId != null,
            isLastGroup = uiState.isLastGroup,
            exercises = uiState.exercises,
            selectedExerciseIds = selectedIdsForCurrentGroup,
            isLoading = uiState.isLoading,
            onBackClick = { viewModel.onBackPressed(onBackToGroupSelection = onBackClick) },
            onNavigate = onNavigate,
            onExerciseToggle = { viewModel.toggleExerciseSelection(it) },
            onInfoClick = { showImageForExerciseId = it },
            onContinueClick = {
                viewModel.onContinueClick(onFinished = onConfigureExercisesClick)
            },
            onStartEditingName = { viewModel.startEditingName() },
            onCancelEditingName = { viewModel.cancelEditingName() },
            onConfirmEditingName = { viewModel.confirmEditingName() },
            onTempNameChange = { viewModel.updateTempName(it) }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectExercisesContent(
    muscleGroupName: String,
    workoutName: String,
    tempWorkoutName: String,
    isEditingName: Boolean,
    exercises: List<ExerciseSelectionUIModel>,
    selectedExerciseIds: Set<String>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onExerciseToggle: (String) -> Unit,
    onInfoClick: (String) -> Unit,
    onContinueClick: () -> Unit,
    onStartEditingName: () -> Unit,
    onCancelEditingName: () -> Unit,
    onConfirmEditingName: () -> Unit,
    onTempNameChange: (String) -> Unit,
    isEditMode: Boolean = false,
    isLastGroup: Boolean = false
) {
    val isButtonEnabled = selectedExerciseIds.isNotEmpty()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarReturn(
                title = if (isEditMode) "Editar exercícios" else "Selecionar exercícios",
                onBackClick = onBackClick,
                isCenterAligned = false
            )
        },
        bottomBar = {
            Column {
                Box(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                        .padding(bottom = Dimens.SpacingMedium)
                ) {
                    Button(
                        onClick = onContinueClick,
                        enabled = isButtonEnabled && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.ButtonHeightPrimary),
                        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = if (isLastGroup) "Configurar exercícios" else "Próximo grupo muscular",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
            ) {
                item {
                    Spacer(modifier = Modifier.height(Dimens.SpacingMediumSmall))
                    if (isEditingName) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = tempWorkoutName,
                                onValueChange = onTempNameChange,
                                modifier = Modifier.weight(1f),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                                ),
                                singleLine = true
                            )
                            IconButton(onClick = onConfirmEditingName) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Confirmar",
                                    tint = Color.Green
                                )
                            }
                            IconButton(onClick = onCancelEditingName) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cancelar",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Nome do treino:",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.width(Dimens.SpacingExtraSmall))
                                Text(
                                    text = workoutName,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                            }
                            IconButton(onClick = onStartEditingName) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar nome",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(Dimens.IconSizeSmall)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                    Text(
                        text = "$muscleGroupName: ${exercises.size} disponíveis",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingExtraSmall))
                }

                items(exercises, key = { it.id }) { exercise ->
                    val isSelected = selectedExerciseIds.contains(exercise.id)
                    ExerciseRowItem(
                        item = exercise,
                        isSelected = isSelected,
                        onCheckedChange = { onExerciseToggle(exercise.id) },
                        onInfoClick = { onInfoClick(exercise.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(Dimens.SpacingLarge)) }
            }
        }
    }
}

@Preview
@Composable
fun SelectExercisesScreenPreview() {
    EvoFitTheme {
        SelectExercisesContent(
            muscleGroupName = "Peito",
            workoutName = "Treino A",
            tempWorkoutName = "",
            isEditingName = false,
            exercises = listOf(
                ExerciseSelectionUIModel("11", "Supino Reto"),
                ExerciseSelectionUIModel("14", "Crucifixo")
            ),
            selectedExerciseIds = setOf("11"),
            isLoading = false,
            onBackClick = {},
            onNavigate = {},
            onExerciseToggle = {},
            onInfoClick = {},
            onContinueClick = {},
            onStartEditingName = {},
            onCancelEditingName = {},
            onConfirmEditingName = {},
            onTempNameChange = {},
            isEditMode = false,
            isLastGroup = false
        )
    }
}
