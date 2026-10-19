package com.guaracode.evofit.presentation.ui.feature.workout.resume.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.guaracode.evofit.presentation.ui.feature.workout.resume.components.WorkoutSummaryCard
import com.guaracode.evofit.presentation.ui.feature.workout.resume.state.ResumeMode
import com.guaracode.evofit.presentation.ui.feature.workout.resume.state.WorkoutResumeUiState
import com.guaracode.evofit.presentation.ui.feature.workout.resume.tracking.WorkoutResumeTracker
import com.guaracode.evofit.presentation.ui.feature.workout.resume.viewmodel.WorkoutResumeViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun WorkoutResumeScreen(
    workoutId: String? = null,
    workoutDoneId: String? = null,
    editWorkoutId: String? = null,
    workoutNotFinishedId: String? = null,
    onContinueClick: () -> Unit,
    viewModel: WorkoutResumeViewModel = koinViewModel { 
        parametersOf(workoutId, workoutDoneId, editWorkoutId, workoutNotFinishedId) 
    },
    tracker: WorkoutResumeTracker = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()

    androidx.compose.runtime.LaunchedEffect(uiState.workoutName) {
        if (uiState.workoutName.isNotEmpty()) {
            tracker.trackWorkoutResumeScreenView(uiState.workoutName)
        }
    }

    WorkoutResumeContent(
        uiState = uiState,
        onContinueClick = onContinueClick
    )
}

@Composable
fun WorkoutResumeContent(
    uiState: WorkoutResumeUiState,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            WorkoutResumeBottomBar(
                isLoading = uiState.isLoading,
                onContinueClick = onContinueClick
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            FullScreenLoader()
        } else {
            ResumeMainContent(uiState, paddingValues)
        }
    }
}

@Composable
private fun ResumeMainContent(
    uiState: WorkoutResumeUiState,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = Dimens.ScreenPaddingHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val (icon, tint) = getModeResources(uiState.mode)

        Box(
            modifier = Modifier
                .size(Dimens.FabSizeDefault)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(Dimens.StatCardIconSize)
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

        Text(
            text = getTitleForMode(uiState.mode),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

        Text(
            text = getSubtitleForMode(uiState.mode, uiState.workoutName),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        if (uiState.mode != ResumeMode.CANCELLED) {
            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))
            WorkoutSummaryCard(
                totalExercises = uiState.totalExercises,
                totalSets = uiState.totalSets,
                completedSets = uiState.completedSets,
                duration = uiState.duration,
                formattedDate = uiState.formattedDate
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingExtraLargePlus))
    }
}

@Composable
private fun getModeResources(mode: ResumeMode): Pair<ImageVector, Color> = when (mode) {
    ResumeMode.CANCELLED -> Icons.Default.Close to MaterialTheme.colorScheme.primary
    ResumeMode.UPDATED -> Icons.Default.Edit to MaterialTheme.colorScheme.primary
    else -> Icons.Default.CheckCircle to MaterialTheme.colorScheme.primary
}

@Composable
private fun getTitleForMode(mode: ResumeMode): String = when (mode) {
    ResumeMode.COMPLETED -> "Treino Concluído!"
    ResumeMode.CANCELLED -> "Treino Cancelado"
    ResumeMode.UPDATED -> "Treino Atualizado!"
    ResumeMode.CREATED -> "Treino Criado!"
}

@Composable
private fun getSubtitleForMode(mode: ResumeMode, name: String): String = when (mode) {
    ResumeMode.COMPLETED -> "Parabéns por concluir $name!"
    ResumeMode.CANCELLED -> "A sessão de $name foi cancelada."
    ResumeMode.UPDATED -> "A ficha $name foi atualizada com sucesso."
    ResumeMode.CREATED -> "Sua ficha $name está pronta para usar."
}

@Composable
private fun WorkoutResumeBottomBar(
    isLoading: Boolean,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(Dimens.SpacingMedium)
    ) {
        Button(
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth().height(Dimens.ButtonHeightPrimary),
            enabled = !isLoading,
            shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Concluir",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun FullScreenLoader() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Preview
@Composable
private fun WorkoutResumeCreatedPreview() {
    EvoFitTheme {
        WorkoutResumeContent(
            uiState = WorkoutResumeUiState(
                workoutName = "Ficha de Peito",
                totalExercises = 5,
                totalSets = 15,
                formattedDate = "25/05/2026",
                mode = ResumeMode.CREATED
            ),
            onContinueClick = {}
        )
    }
}
