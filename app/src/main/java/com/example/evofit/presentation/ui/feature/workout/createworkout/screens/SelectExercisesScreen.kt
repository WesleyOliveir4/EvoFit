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
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.components.EvoFitActionDialog
import com.example.evofit.presentation.ui.feature.workout.components.configure.ExerciseRowItem
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SelectExercisesViewModel
import com.example.evofit.presentation.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectExercisesScreen(
    muscleGroupId: String,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onConfigureExercisesClick: (List<String>, String, String?) -> Unit,
    editWorkoutId: String? = null,
    viewModel: SelectExercisesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedIds = viewModel.selectedExerciseIds

    LaunchedEffect(muscleGroupId, editWorkoutId) {
        viewModel.loadExercises(muscleGroupId, editWorkoutId)
    }

    BackHandler { viewModel.onBackPressed(onProceed = onBackClick) }

    SelectExercisesContent(
        muscleGroupName = uiState.muscleGroupName,
        workoutName = uiState.workoutName,
        tempWorkoutName = uiState.tempWorkoutName,
        isEditingName = uiState.isEditingName,
        isEditMode = uiState.editWorkoutId != null,
        exercises = uiState.exercises,
        selectedExerciseIds = selectedIds,
        isLoading = uiState.isLoading,
        onBackClick = { viewModel.onBackPressed(onProceed = onBackClick) },
        onNavigate = onNavigate,
        onExerciseToggle = { viewModel.toggleExerciseSelection(it) },
        onConfigureExercisesClick = {
            onConfigureExercisesClick(selectedIds, uiState.workoutName, uiState.editWorkoutId)
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
    selectedExerciseIds: List<String>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onExerciseToggle: (String) -> Unit,
    onConfigureExercisesClick: () -> Unit,
    onStartEditingName: () -> Unit,
    onCancelEditingName: () -> Unit,
    onConfirmEditingName: () -> Unit,
    onTempNameChange: (String) -> Unit,
    isEditMode: Boolean = false
) {
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
                        onClick = onConfigureExercisesClick,
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
                            text = stringResource(R.string.select_exercises_button_configure),
                            // Alterado para usar cores do tema ao invés de Color.Black fixo
                            color = if (isButtonEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary,
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
                                    tint = MaterialTheme.colorScheme.onBackground // Alterado de Color.White para onBackground
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
                    // Agora utiliza o muscleGroupName que estava sobrando
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
            selectedExerciseIds = listOf("1", "2"),
            isLoading = false,
            onBackClick = {},
            onNavigate = {},
            onExerciseToggle = {},
            onConfigureExercisesClick = {},
            onStartEditingName = {},
            onCancelEditingName = {},
            onConfirmEditingName = {},
            onTempNameChange = {},
            isEditMode = false
        )
    }
}