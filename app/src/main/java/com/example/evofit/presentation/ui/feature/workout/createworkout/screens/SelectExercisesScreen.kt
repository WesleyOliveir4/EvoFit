package com.example.evofit.presentation.ui.feature.workout.createworkout.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
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
import com.example.evofit.presentation.model.ExerciseSelectionUIModel
import com.example.evofit.presentation.ui.feature.components.EvoFitActionDialog
import com.example.evofit.presentation.ui.feature.workout.components.configure.ExerciseRowItem
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SelectExercisesViewModel
import com.example.evofit.presentation.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectExercisesScreen(
    muscleGroupIds: List<String>,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onConfigureExercisesClick: (List<String>, String, String?) -> Unit,
    editWorkoutId: String? = null,
    viewModel: SelectExercisesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentGroupId = uiState.muscleGroupIds.getOrNull(uiState.currentGroupIndex) ?: ""
    val selectedIdsForCurrentGroup = uiState.allSelectedExerciseIds[currentGroupId] ?: emptySet()

    LaunchedEffect(muscleGroupIds, editWorkoutId) {
        viewModel.loadInitialData(muscleGroupIds, editWorkoutId)
    }

    BackHandler { viewModel.onBackPressed(onBackToGroupSelection = onBackClick) }

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
        onContinueClick = {
            viewModel.onContinueClick(onFinished = onConfigureExercisesClick)
        },
        onStartEditingName = { viewModel.startEditingName() },
        onCancelEditingName = { viewModel.cancelEditingName() },
        onConfirmEditingName = { viewModel.confirmEditingName() },
        onTempNameChange = { viewModel.updateTempName(it) }
    )

    if (uiState.showCancelEditDialog) {
        EvoFitActionDialog(
            title = stringResource(R.string.select_exercises_cancel_edit_dialog_title),
            description = stringResource(R.string.select_exercises_cancel_edit_dialog_message),
            icon = Icons.Default.EditOff,
            confirmButtonText = stringResource(R.string.select_exercises_cancel_edit_dialog_confirm),
            dismissButtonText = stringResource(R.string.select_exercises_cancel_edit_dialog_cancel),
            onConfirm = { viewModel.onConfirmCancelEdit(onProceed = onBackClick) },
            onDismiss = { viewModel.onDismissCancelEditDialog() }
        )
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
    onContinueClick: () -> Unit,
    onStartEditingName: () -> Unit,
    onCancelEditingName: () -> Unit,
    onConfirmEditingName: () -> Unit,
    onTempNameChange: (String) -> Unit,
    isEditMode: Boolean = false,
    isLastGroup: Boolean = false
) {
    // Enable button only if there's at least one exercise selected in TOTAL if it's the last group,
    // or just enable it to allow moving forward? The user said "Só habilita se tiver selecionado no mínimo 1 grupo muscular" for the first screen.
    // For this screen, let's keep it consistent: need at least one exercise to continue to next group or finish.
    val isButtonEnabled = selectedExerciseIds.isNotEmpty()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) {
                            stringResource(R.string.select_exercises_edit_title)
                        } else {
                            stringResource(R.string.select_exercises_title)
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.select_exercises_back_desc),
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
            Column {
                Box(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onContinueClick,
                        enabled = isButtonEnabled && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = if (isLastGroup) "Configurar exercícios" else "Próximo grupo muscular",
                            color = if (isButtonEnabled) Color.Black else MaterialTheme.colorScheme.secondary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    if (isEditingName) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = tempWorkoutName,
                                onValueChange = onTempNameChange,
                                modifier = Modifier.weight(1f),
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
                                    contentDescription = stringResource(R.string.select_exercises_confirm_desc),
                                    tint = Color.Green
                                )
                            }
                            IconButton(onClick = onCancelEditingName) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.select_exercises_cancel_desc),
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(R.string.select_exercises_label_name),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = workoutName,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = onStartEditingName) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.select_exercises_edit_name_desc),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "$muscleGroupName: " + stringResource(R.string.select_exercises_available_count, exercises.size),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(exercises, key = { it.id }) { exercise ->
                    val isSelected = selectedExerciseIds.contains(exercise.id)
                    ExerciseRowItem(
                        item = exercise,
                        isSelected = isSelected,
                        onCheckedChange = { onExerciseToggle(exercise.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectExercisesScreenPreview() {
    EvoFitTheme {
        SelectExercisesContent(
            muscleGroupName = "Peito",
            workoutName = "Treino A",
            tempWorkoutName = "",
            isEditingName = false,
            exercises = listOf(
                ExerciseSelectionUIModel("1", "Supino Reto"),
                ExerciseSelectionUIModel("2", "Crucifixo Inclinado"),
                ExerciseSelectionUIModel("3", "Crossover"),
                ExerciseSelectionUIModel("4", "Flexão de Braços")
            ),
            selectedExerciseIds = setOf("1", "2"),
            isLoading = false,
            onBackClick = {},
            onNavigate = {},
            onExerciseToggle = {},
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
