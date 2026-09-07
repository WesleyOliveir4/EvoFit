package com.example.evofit.presentation.ui.feature.workout.createworkout.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.model.ExerciseSelectionUIModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.feature.workout.components.configure.ExerciseRowItem
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SelectExercisesViewModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.evofit.presentation.mapper.ExerciseMapper

// ... (imports)

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
    
    var showImageForExerciseId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(muscleGroupIds, editWorkoutId) {
        viewModel.loadInitialData(muscleGroupIds, editWorkoutId)
    }

    BackHandler { 
        if (showImageForExerciseId != null) {
            showImageForExerciseId = null
        } else {
            viewModel.onBackPressed(onBackToGroupSelection = onBackClick)
        }
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
                        painter = painterResource(id = ExerciseMapper.toImageRes(exerciseId)),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .offset(y = (-50).dp)
                            .clip(RoundedCornerShape(Dimens.CornerRadiusLarge))
                            .clickable(enabled = false) {}, // Prevent closing when clicking on image
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
    // ... (rest of the content)
    // Enable button only if there's at least one exercise selected in TOTAL if it's the last group,
    // or just enable it to allow moving forward? The user said "Só habilita se tiver selecionado no mínimo 1 grupo muscular" for the first screen.
    // For this screen, let's keep it consistent: need at least one exercise to continue to next group or finish.
    val isButtonEnabled = selectedExerciseIds.isNotEmpty()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarReturn(
                title = if (isEditMode) {
                    stringResource(R.string.select_exercises_edit_title)
                } else {
                    stringResource(R.string.select_exercises_title)
                },
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
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.select_exercises_label_name),
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
                                    contentDescription = stringResource(R.string.select_exercises_edit_name_desc),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(Dimens.IconSizeSmall)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                    Text(
                        text = "$muscleGroupName: " + stringResource(R.string.select_exercises_available_count, exercises.size),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectExercisesScreenPreview() {
    EvoFitTheme {
        SelectExercisesContent(
            muscleGroupName = "Chest",
            workoutName = "Workout A",
            tempWorkoutName = "",
            isEditingName = false,
            exercises = listOf(
                ExerciseSelectionUIModel("11", "Bench Press"),
                ExerciseSelectionUIModel("14", "Chest Fly"),
                ExerciseSelectionUIModel("17", "Crossover"),
                ExerciseSelectionUIModel("18", "Push-up")
            ),
            selectedExerciseIds = setOf("11", "14"),
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
