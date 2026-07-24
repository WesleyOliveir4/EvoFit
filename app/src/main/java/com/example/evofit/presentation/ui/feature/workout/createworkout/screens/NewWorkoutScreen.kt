package com.example.evofit.presentation.ui.feature.workout.createworkout.screens

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.model.MuscleGroupItem
import com.example.evofit.presentation.ui.feature.workout.components.configure.MuscleGroupCard
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.NewWorkoutViewModel
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

    NewWorkoutContent(
        muscleGroups = uiState.muscleGroups,
        selectedMuscleGroupIds = uiState.selectedMuscleGroupIds,
        isLoading = uiState.isLoading,
        onBackClick = onBackClick,
        onMuscleGroupClick = { groupId ->
            viewModel.toggleMuscleGroupSelection(groupId)
        },
        onContinueClick = {
            onSelectExercisesClick(uiState.selectedMuscleGroupIds.toList(), editWorkoutId)
        }
    )
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
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_workout_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.new_workout_back_desc),
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
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.navigationBars)
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
                        text = stringResource(R.string.select_exercises_title), // "Selecionar exercícios"
                        color = if (isButtonEnabled) Color.Black else MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Selecione os grupos musculares para seu treino",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(muscleGroups, key = { it.id }) { item ->
                    MuscleGroupCard(
                        item = item,
                        isSelected = selectedMuscleGroupIds.contains(item.id),
                        onClick = { onMuscleGroupClick(item.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
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
                MuscleGroupItem(
                    id = "1",
                    name = "Peito",
                    temporaryIcon = Icons.Default.Favorite
                )
            ),
            selectedMuscleGroupIds = setOf("1"),
            isLoading = false,
            onBackClick = {},
            onMuscleGroupClick = {},
            onContinueClick = {}
        )
    }
}
