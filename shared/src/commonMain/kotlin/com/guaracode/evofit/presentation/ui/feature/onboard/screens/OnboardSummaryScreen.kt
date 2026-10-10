package com.guaracode.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitButton
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.onboard.state.OnboardingUiState
import com.guaracode.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import com.guaracode.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject

@Composable
fun OnboardSummaryScreen(
    currentPage: Int,
    totalPages: Int,
    onStartTraining: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
    tracker: OnboardingTracker = koinInject()
) {
    val userData by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        tracker.trackOnboardingSummaryScreenView()
    }

    OnboardSummaryContent(
        userData = userData,
        currentPage = currentPage,
        totalPages = totalPages,
        onStartTraining = remember { 
            { 
                viewModel.finishOnboarding(onStartTraining) 
            }
        },
        onBack = onBack
    )
}

@Composable
fun OnboardSummaryContent(
    userData: OnboardingUiState,
    currentPage: Int,
    totalPages: Int,
    onStartTraining: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        topBar = {
            TopBarReturn(
                onBackClick = onBack
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingExtraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PageIndicators(
                    pageCount = totalPages,
                    selectedPage = currentPage,
                    modifier = Modifier.padding(bottom = Dimens.SpacingMedium)
                )

                EvoFitButton(
                    text = "Começar a treinar",
                    onClick = onStartTraining
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Tudo pronto!",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                    Text(
                        text = "Confira seu resumo antes de iniciar.",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpacingSmall),
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.SpacingLarge),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
                ) {
                    Text(
                        text = "RESUMO DO PERFIL",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        letterSpacing = 1.sp
                    )

                    SummaryRow(
                        icon = Icons.Default.AccountCircle,
                        label = "Nome",
                        value = userData.name
                    )

                    SummaryRow(
                        icon = Icons.Default.DateRange,
                        label = "Data de nascimento",
                        value = userData.birthDate
                    )

                    SummaryRow(
                        icon = Icons.Default.Favorite,
                        label = "Peso",
                        value = "${userData.weight} kg"
                    )

                    SummaryRow(
                        icon = Icons.Default.Star,
                        label = "Altura",
                        value = "${userData.height} cm"
                    )

                    if (userData.goals.isNotEmpty()) {
                        SummaryRow(
                            icon = Icons.Default.Check,
                            label = "Metas",
                            value = "${userData.goals.size}"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}

@Composable
fun SummaryRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.MinimumTouchTarget)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.IconSizeDefault)
            )
        }

        Column {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
