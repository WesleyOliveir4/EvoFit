package com.example.evofit.presentation.ui.feature.workout.resume.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.workout.resume.components.WorkoutSummaryCard
import com.example.evofit.presentation.ui.feature.workout.resume.state.ResumeMode
import com.example.evofit.presentation.ui.feature.workout.resume.state.WorkoutResumeUiState
import com.example.evofit.presentation.ui.feature.workout.resume.viewmodel.WorkoutResumeViewModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutResumeScreen(
    workoutId: String? = null,
    workoutDoneId: String? = null,
    editWorkoutId: String? = null,
    workoutNotFinishedId: String? = null,
    onContinueClick: () -> Unit,
    viewModel: WorkoutResumeViewModel = koinViewModel { 
        parametersOf(workoutId, workoutDoneId, editWorkoutId, workoutNotFinishedId) 
    }
) {
    val uiState by viewModel.uiState.collectAsState()

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
    ResumeMode.CANCELLED -> ImageVector.vectorResource(id = R.drawable.ic_clipboard_off) to MaterialTheme.colorScheme.primary
    ResumeMode.UPDATED -> Icons.Default.Edit to MaterialTheme.colorScheme.primary
    else -> Icons.Default.CheckCircle to MaterialTheme.colorScheme.primary
}

@Composable
private fun getTitleForMode(mode: ResumeMode): String = when (mode) {
    ResumeMode.COMPLETED -> stringResource(R.string.workout_resume_done_title)
    ResumeMode.CANCELLED -> stringResource(R.string.workout_resume_cancelled_title)
    ResumeMode.UPDATED -> stringResource(R.string.workout_resume_updated_title)
    ResumeMode.CREATED -> stringResource(R.string.workout_resume_title)
}

@Composable
private fun getSubtitleForMode(mode: ResumeMode, name: String): String = when (mode) {
    ResumeMode.COMPLETED -> stringResource(R.string.workout_done_resume_subtitle, name)
    ResumeMode.CANCELLED -> stringResource(R.string.workout_resume_cancelled_subtitle, name)
    ResumeMode.UPDATED -> stringResource(R.string.workout_resume_updated_subtitle, name)
    ResumeMode.CREATED -> stringResource(R.string.workout_resume_subtitle, name)
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
                text = stringResource(R.string.workout_resume_button_confirm),
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

@Preview(name = "Treino Criado", showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutResumeCreatedPreview() {
    EvoFitTheme {
        WorkoutResumeContent(
            uiState = WorkoutResumeUiState(
                workoutName = "Ficha de Peito",
                totalExercises = 5,
                totalSets = 15,
                formattedDate = "25/05/2024",
                mode = ResumeMode.CREATED
            ),
            onContinueClick = {}
        )
    }
}

@Preview(name = "Treino Atualizado", showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutResumeUpdatedPreview() {
    EvoFitTheme {
        WorkoutResumeContent(
            uiState = WorkoutResumeUiState(
                workoutName = "Ficha de Costas",
                totalExercises = 6,
                totalSets = 18,
                formattedDate = "25/05/2024",
                mode = ResumeMode.UPDATED
            ),
            onContinueClick = {}
        )
    }
}

@Preview(name = "Treino Finalizado", showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutResumeCompletedPreview() {
    EvoFitTheme {
        WorkoutResumeContent(
            uiState = WorkoutResumeUiState(
                workoutName = "Treino de Pernas",
                totalExercises = 4,
                totalSets = 12,
                completedSets = 10,
                duration = "00:52:15",
                formattedDate = "25/05/2024",
                mode = ResumeMode.COMPLETED
            ),
            onContinueClick = {}
        )
    }
}

@Preview(name = "Treino Cancelado", showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutResumeCancelledPreview() {
    EvoFitTheme {
        WorkoutResumeContent(
            uiState = WorkoutResumeUiState(
                workoutName = "Treino de Ombro",
                mode = ResumeMode.CANCELLED
            ),
            onContinueClick = {}
        )
    }
}
