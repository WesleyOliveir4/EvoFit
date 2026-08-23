package com.example.evofit.presentation.ui.feature.workout.createworkout.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.model.MuscleGroupItem
import com.example.evofit.presentation.ui.feature.components.EvoFitActionDialog
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.feature.workout.components.configure.MuscleGroupCard
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.NewWorkoutViewModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewWorkoutScreen(
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onSelectExercisesClick: (List<String>, String?) -> Unit,
    editWorkoutId: String? = null,
    viewModel: NewWorkoutViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(editWorkoutId) {
        viewModel.loadMuscleGroups(editWorkoutId)
    }

    BackHandler {
        viewModel.onBackPressed(onProceed = onBackClick)
    }

    NewWorkoutContent(
        muscleGroups = uiState.muscleGroups,
        selectedMuscleGroupIds = uiState.selectedMuscleGroupIds,
        isLoading = uiState.isLoading,
        onBackClick = {
            viewModel.onBackPressed(onProceed = onBackClick)
        },
        onMuscleGroupClick = { groupId ->
            viewModel.toggleMuscleGroupSelection(groupId)
        },
        onContinueClick = {
            onSelectExercisesClick(uiState.selectedMuscleGroupIds.toList(), editWorkoutId)
        }
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
fun NewWorkoutContent(
    muscleGroups: List<MuscleGroupItem>,
    selectedMuscleGroupIds: Set<String>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onMuscleGroupClick: (String) -> Unit,
    onContinueClick: () -> Unit
) {
    val isButtonEnabled = selectedMuscleGroupIds.isNotEmpty()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarReturn(
                title = stringResource(R.string.new_workout_title),
                onBackClick = onBackClick,
                isCenterAligned = false
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.navigationBars)
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
                        text = stringResource(R.string.select_exercises_title), // "Selecionar exercícios"
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
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
                    Text(
                        text = "Selecione os grupos musculares para seu treino",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                }

                items(muscleGroups, key = { it.id }) { item ->
                    MuscleGroupCard(
                        item = item,
                        isSelected = selectedMuscleGroupIds.contains(item.id),
                        onClick = { onMuscleGroupClick(item.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(Dimens.SpacingLarge)) }
            }
        }
    }
}

@Preview
@Composable
fun NewWorkoutScreenPreview() {
    EvoFitTheme {
        NewWorkoutContent(
            muscleGroups = listOf(
                MuscleGroupItem("1", "Costas", imageRes = R.drawable.ic_back),
                MuscleGroupItem("2", "Peito", imageRes = R.drawable.ic_chest),
                MuscleGroupItem("3", "Pernas", imageRes = R.drawable.ic_legs),
                MuscleGroupItem("4", "Braços", imageRes = R.drawable.ic_arms),
                MuscleGroupItem("5", "Ombros", imageRes = R.drawable.ic_shoulder),
                MuscleGroupItem("6", "Core", imageRes = R.drawable.ic_abs),
                MuscleGroupItem("7", "Cardio", imageRes = R.drawable.ic_cardio),
                MuscleGroupItem("8", "Gluteo", imageRes = R.drawable.ic_gluteus),
                MuscleGroupItem("9", "Panturrilha", imageRes = R.drawable.ic_calf),
            ),
            selectedMuscleGroupIds = setOf("1", "2"),
            isLoading = false,
            onBackClick = {},
            onMuscleGroupClick = {},
            onContinueClick = {}
        )
    }
}
