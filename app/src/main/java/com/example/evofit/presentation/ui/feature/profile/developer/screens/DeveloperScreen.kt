package com.example.evofit.presentation.ui.feature.profile.developer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.feature.profile.developer.viewmodel.DeveloperViewModel
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.Dimens
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeveloperScreen(
    viewModel: DeveloperViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }

    LaunchedEffect(uiState.generationSuccess) {
        if (uiState.generationSuccess) {
            snackbarHostState.showSnackbar("Histórico gerado com sucesso!")
            viewModel.resetSuccessState()
        }
    }

    Scaffold(
        containerColor = AppDarkBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopBarReturn(
                onBackClick = onBackClick,
                title = "Menu do Desenvolvedor"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.SpacingMedium),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BugReport,
                            contentDescription = null,
                            tint = Color.Red
                        )
                        Text(
                            text = "Geração de Dados",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    Text(
                        text = "Esta opção irá gerar 6 meses de histórico de treinos fictícios (4 treinos por semana) com evolução progressiva de 5% ao mês.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Button(
                        onClick = { viewModel.generateFakeHistory() },
                        enabled = !uiState.isGeneratingHistory,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        if (uiState.isGeneratingHistory) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(Dimens.IconSizeSmall),
                                color = MaterialTheme.colorScheme.onTertiary,
                                strokeWidth = Dimens.BorderWidthThin
                            )
                            Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                            Text("Gerando...")
                        } else {
                            Text("Adicionar 6 meses de treino")
                        }
                    }
                }
            }
        }
    }
}
