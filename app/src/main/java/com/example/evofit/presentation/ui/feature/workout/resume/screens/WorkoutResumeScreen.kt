package com.example.evofit.presentation.ui.feature.workout.resume.screens

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.workout.resume.components.WorkoutSummaryCard
import com.example.evofit.presentation.ui.feature.workout.resume.viewmodel.WorkoutResumeViewModel
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.AppGreen
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.IconContainerBg
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutResumeScreen(
    workoutId: Long,
    onContinueClick: () -> Unit,
    viewModel: WorkoutResumeViewModel = koinViewModel { parametersOf(workoutId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    WorkoutResumeContent(
        workoutName = uiState.workoutName,
        totalExercises = uiState.totalExercises,
        totalSets = uiState.totalSets,
        formattedDate = uiState.formattedDate,
        onContinueClick = onContinueClick,
        isLoading = uiState.isLoading
    )
}

@Composable
fun WorkoutResumeContent(
    workoutName: String,
    totalExercises: Int,
    totalSets: Int,
    formattedDate: String,
    onContinueClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppDarkBg,
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(AppDarkBg)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppGreen)
                ) {
                    Text(
                        text = stringResource(R.string.workout_resume_button_confirm),
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 1. Ícone Superior de Confirmação (Sucesso)
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(IconContainerBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AppGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Título de Sucesso
                Text(
                    text = stringResource(R.string.workout_resume_title),
                    color = TextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 3. Subtítulo com o nome do treino injetado dinamicamente
                Text(
                    text = stringResource(R.string.workout_resume_subtitle, workoutName),
                    color = TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 4. Card de Resumo das Estatísticas do Treino
                WorkoutSummaryCard(
                    totalExercises = totalExercises,
                    totalSets = totalSets,
                    formattedDate = formattedDate
                )

                // Pequeno espaçamento extra para equilibrar o peso visual com a bottom bar
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutResumeScreenPreview() {
    EvoFitTheme {
        WorkoutResumeContent(
            workoutName = "Costas predio",
            totalExercises = 4,
            totalSets = 10,
            formattedDate = "25/05/2024",
            onContinueClick = {}
        )
    }
}
