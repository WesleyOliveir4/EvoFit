package com.example.evofit.presentation.ui.feature.evo.analytics.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.domain.usecase.ExerciseWithRecords
import com.example.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.IconContainerBg

@Composable
fun ExerciseSelectionScreen(
    viewModel: EvoAnalyticsViewModel,
    onBackClick: () -> Unit = {},
    onExerciseClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExerciseSelectionContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onExerciseClick = { exerciseId, exerciseName ->
            viewModel.onExerciseSelected(exerciseId, exerciseName)
            onExerciseClick(exerciseId)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSelectionContent(
    uiState: EvoAnalyticsState,
    onBackClick: () -> Unit,
    onExerciseClick: (String, String) -> Unit
) {
    var selectedExerciseId by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.muscleGroupName,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.evo_analytics_select_exercise),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.evo_analytics_back_desc),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.exercisesForSelection.isEmpty()) {
                Text(
                    text = "Nenhum exercício encontrado.",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(uiState.exercisesForSelection, key = { it.exercise.id }) { item ->
                        val isSelected = item.exercise.id == selectedExerciseId

                        ExerciseItemCard(
                            name = item.exercise.name,
                            recordsCount = item.recordsCount,
                            isSelected = isSelected,
                            onClick = {
                                selectedExerciseId = item.exercise.id
                                onExerciseClick(item.exercise.id, item.exercise.name)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseItemCard(
    name: String,
    recordsCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(24.dp)
                )
                else Modifier
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) IconContainerBg else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.evo_analytics_records_format, recordsCount),
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseSelectionScreenPreview() {
    EvoFitTheme {
        ExerciseSelectionContent(
            uiState = EvoAnalyticsState(
                muscleGroupName = "Costas",
                isLoading = false
            ),
            onBackClick = {},
            onExerciseClick = { _, _ -> }
        )
    }
}
