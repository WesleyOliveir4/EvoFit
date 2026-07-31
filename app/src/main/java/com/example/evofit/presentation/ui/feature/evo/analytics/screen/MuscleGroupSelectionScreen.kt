package com.example.evofit.presentation.ui.feature.evo.analytics.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.evo.analytics.components.MuscleGroup
import com.example.evofit.presentation.ui.feature.evo.analytics.components.MuscleGroupCard
import com.example.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MuscleGroupSelectionScreen(
    onBackClick: () -> Unit = {},
    onGroupSelected: (String, String) -> Unit = { _, _ -> },
    viewModel: EvoAnalyticsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MuscleGroupSelectionContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onGroupSelected = { id, name ->
            viewModel.onMuscleGroupSelected(id, name)
            onGroupSelected(id, name)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupSelectionContent(
    uiState: EvoAnalyticsState,
    onBackClick: () -> Unit,
    onGroupSelected: (String, String) -> Unit
) {
    var selectedGroupId by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarReturn(
                title = stringResource(R.string.evo_analytics_select_muscle_group),
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
            } else if (uiState.trainedGroups.isEmpty()) {
                Text(
                    text = stringResource(R.string.evo_analytics_empty_history),
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingSmall),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
                ) {
                    items(uiState.trainedGroups, key = { it.id }) { groupItem ->
                        val isSelected = groupItem.id == selectedGroupId
                        val uiGroup = remember(groupItem) {
                            MuscleGroup(
                                name = groupItem.name,
                                icon = groupItem.temporaryIcon
                            )
                        }

                        MuscleGroupCard(
                            group = uiGroup,
                            isSelected = isSelected,
                            onClick = {
                                selectedGroupId = groupItem.id
                                onGroupSelected(groupItem.id, groupItem.name)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun MuscleGroupSelectionScreenPreview() {
    EvoFitTheme {
        MuscleGroupSelectionContent(
            uiState = EvoAnalyticsState(
                trainedGroups = emptyList(),
                isLoading = false
            ),
            onBackClick = {},
            onGroupSelected = { _, _ -> }
        )
    }
}
