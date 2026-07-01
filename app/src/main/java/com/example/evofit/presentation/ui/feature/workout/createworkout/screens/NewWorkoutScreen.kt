package com.example.evofit.presentation.ui.feature.workout.createworkout.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.data.model.MuscleGroupType
import com.example.evofit.presentation.mapper.toItem
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.workout.components.MuscleGroupCard
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.NewWorkoutViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewWorkoutScreen(
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onGroupSelected: (String) -> Unit,
    viewModel: NewWorkoutViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NewWorkoutContent(
        muscleGroups = uiState.muscleGroups,
        isLoading = uiState.isLoading,
        onBackClick = onBackClick,
        onNavigate = onNavigate,
        onMuscleGroupClick = { groupId ->
            onGroupSelected(groupId)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewWorkoutContent(
    muscleGroups: List<MuscleGroupModel>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onMuscleGroupClick: (String) -> Unit
) {
    val muscleGroupItems = muscleGroups.map { it.toItem() }

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
            AppBottomNavigation(
                currentRoute = "new_workout",
                onNavigate = onNavigate
            )
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
                        text = stringResource(R.string.new_workout_select_group),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(muscleGroupItems) { item ->
                    MuscleGroupCard(
                        item = item,
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
                MuscleGroupModel(
                    id = "1",
                    name = "Peito",
                    type = MuscleGroupType.CHEST
                )
            ),
            isLoading = false,
            onBackClick = {},
            onNavigate = {},
            onMuscleGroupClick = {}
        )
    }
}
