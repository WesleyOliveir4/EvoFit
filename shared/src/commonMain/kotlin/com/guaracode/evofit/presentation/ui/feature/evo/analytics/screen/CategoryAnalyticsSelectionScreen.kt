package com.guaracode.evofit.presentation.ui.feature.evo.analytics.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.guaracode.evofit.presentation.model.MuscleGroupItem
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.components.MuscleGroup
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.components.MuscleGroupCard
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTracker
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.analytics_body_weight
import evofit.shared.generated.resources.evo_analytics_select_muscle_group
import evofit.shared.generated.resources.ic_balance_2
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun CategoryAnalyticsSelectionScreen(
    onBackClick: () -> Unit = {},
    onGroupSelected: (String, String) -> Unit = { _, _ -> },
    onWeightSelected: () -> Unit = {},
    viewModel: EvoAnalyticsViewModel,
    tracker: EvoAnalyticsTracker = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        tracker.trackCategoryAnalyticsSelectionScreenView()
    }

    CategoryAnalyticsSelectionContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onGroupSelected = { id, name ->
            viewModel.onMuscleGroupSelected(id, name)
            onGroupSelected(id, name)
        },
        onWeightSelected = {
            viewModel.onWeightSelected()
            onWeightSelected()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAnalyticsSelectionContent(
    uiState: EvoAnalyticsState,
    onBackClick: () -> Unit,
    onGroupSelected: (String, String) -> Unit,
    onWeightSelected: () -> Unit
) {
    var selectedGroupId by remember { mutableStateOf("") }
    val bodyWeightLabel = stringResource(Res.string.analytics_body_weight)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarReturn(
                title = stringResource(Res.string.evo_analytics_select_muscle_group),
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
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingSmall),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
                ) {
                    item {
                        MuscleGroupCard(
                            group = MuscleGroup(
                                name = bodyWeightLabel,
                                imageRes = Res.drawable.ic_balance_2
                            ),
                            isSelected = selectedGroupId == "WEIGHT_HISTORY",
                            onClick = {
                                selectedGroupId = "WEIGHT_HISTORY"
                                onWeightSelected()
                            }
                        )
                    }

                    items(uiState.trainedGroups, key = { it.id }) { groupItem ->
                        val isSelected = groupItem.id == selectedGroupId
                        val uiGroup = remember(groupItem) {
                            MuscleGroup(
                                name = groupItem.name,
                                imageRes = groupItem.imageRes
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

@Preview
@Composable
private fun CategoryAnalyticsSelectionScreenPreview() {
    EvoFitTheme {
        CategoryAnalyticsSelectionContent(
            uiState = EvoAnalyticsState(
                trainedGroups = listOf(
                    MuscleGroupItem("1", "Back", Res.drawable.ic_balance_2),
                    MuscleGroupItem("2", "Chest", Res.drawable.ic_balance_2),
                ),
                isLoading = false
            ),
            onBackClick = {},
            onGroupSelected = { _, _ -> },
            onWeightSelected = {}
        )
    }
}
