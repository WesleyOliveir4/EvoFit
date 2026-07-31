package com.example.evofit.presentation.ui.feature.evo.analytics.screen

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

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
            TopBarReturn(
                title = uiState.muscleGroupName,
                subtitle = stringResource(R.string.evo_analytics_select_exercise),
                onBackClick = onBackClick,
                isCenterAligned = false
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
                        .padding(horizontal = Dimens.ScreenPaddingHorizontal),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall),
                    contentPadding = PaddingValues(vertical = Dimens.ScreenPaddingHorizontal)
                ) {
                    items(uiState.exercisesForSelection, key = { it.id }) { item ->
                        val isSelected = item.id == selectedExerciseId

                        ExerciseItemCard(
                            name = item.name,
                            recordsCount = item.recordsCount,
                            isSelected = isSelected,
                            onClick = {
                                selectedExerciseId = item.id
                                onExerciseClick(item.id, item.name)
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
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
        label = "containerColor"
    )
    val nameColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        label = "nameColor"
    )
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        label = "iconColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.EvoCardHeightExtraSmall)
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(
                    width = Dimens.BorderWidthThin,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(Dimens.CornerRadiusCard)
                )
                else Modifier
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.SpacingLarge),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
            ) {
                Text(
                    text = name,
                    color = nameColor,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.evo_analytics_records_format, recordsCount),
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(Dimens.IconSizeSmall)
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
