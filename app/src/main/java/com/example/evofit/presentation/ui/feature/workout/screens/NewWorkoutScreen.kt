package com.example.evofit.presentation.ui.feature.workout.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.workout.components.MuscleGroupCard
import com.example.evofit.presentation.ui.feature.workout.components.MuscleGroupItem
import com.example.evofit.presentation.ui.feature.workout.viewmodel.NewWorkoutViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun NewWorkoutScreen(
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onGroupSelected: (String) -> Unit,
    viewModel: NewWorkoutViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.workoutSaved.collect {
            onBackClick()
        }
    }

    NewWorkoutContent(
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
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onMuscleGroupClick: (String) -> Unit
) {
    // Lista sincronizada com LocalExerciseDataSource
    val muscleGroups = listOf(
        MuscleGroupItem("2", "Peito", Icons.Default.Favorite),
        MuscleGroupItem("1", "Costas", Icons.AutoMirrored.Filled.ArrowBack),
        MuscleGroupItem("5", "Ombros", Icons.Default.Face),
        MuscleGroupItem("4", "Braços", Icons.Default.EmojiPeople),
        MuscleGroupItem("3", "Pernas", Icons.Default.DirectionsRun),
        MuscleGroupItem("6", "Abdômen", Icons.Default.Whatshot),
        MuscleGroupItem("7", "Cardio", Icons.Default.DirectionsRun),
        MuscleGroupItem("8", "Glúteo", Icons.Default.Accessibility),
        MuscleGroupItem("9", "Panturrilha", Icons.Default.DirectionsRun)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Novo Treino",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Instrução da Tela
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Selecione o grupo muscular",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Lista renderizada dinamicamente
            items(muscleGroups) { item ->
                MuscleGroupCard(
                    item = item,
                    onClick = { onMuscleGroupClick(item.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Preview
@Composable
fun NewWorkoutScreenPreview() {
    EvoFitTheme {
        NewWorkoutContent(
            onBackClick = {},
            onNavigate = {},
            onMuscleGroupClick = {}
        )
    }
}
