package com.guaracode.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitButton
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.guaracode.evofit.presentation.ui.feature.onboard.state.OnboardingUiState
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import com.guaracode.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_balance
import evofit.shared.generated.resources.ic_medal
import evofit.shared.generated.resources.ic_ruller
import evofit.shared.generated.resources.onboarding_summary_button_start_training
import evofit.shared.generated.resources.onboarding_summary_description
import evofit.shared.generated.resources.onboarding_summary_label_birth_date
import evofit.shared.generated.resources.onboarding_summary_label_goals
import evofit.shared.generated.resources.onboarding_summary_label_height
import evofit.shared.generated.resources.onboarding_summary_label_name
import evofit.shared.generated.resources.onboarding_summary_label_summary
import evofit.shared.generated.resources.onboarding_summary_label_weight
import evofit.shared.generated.resources.onboarding_summary_title
import evofit.shared.generated.resources.onboarding_summary_value_height
import evofit.shared.generated.resources.onboarding_summary_value_weight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardSummaryScreen(
    currentPage: Int,
    totalPages: Int,
    onStartTraining: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
    tracker: OnboardingTracker = koinInject()
) {
    val userData by viewModel.uiState.collectAsState()

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

@OptIn(ExperimentalMaterial3Api::class)
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
                    text = stringResource(Res.string.onboarding_summary_button_start_training),
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
                        text = stringResource(Res.string.onboarding_summary_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                    Text(
                        text = stringResource(Res.string.onboarding_summary_description),
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
                        text = stringResource(Res.string.onboarding_summary_label_summary),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        letterSpacing = 1.sp
                    )

                    SummaryRowWithVector(
                        icon = Icons.Default.AccountCircle,
                        label = stringResource(Res.string.onboarding_summary_label_name),
                        value = userData.name
                    )

                    SummaryRowWithVector(
                        icon = Icons.Default.DateRange,
                        label = stringResource(Res.string.onboarding_summary_label_birth_date),
                        value = userData.birthDate
                    )

                    SummaryRowWithPainter(
                        painterRes = Res.drawable.ic_balance,
                        label = stringResource(Res.string.onboarding_summary_label_weight),
                        value = stringResource(Res.string.onboarding_summary_value_weight, userData.weight)
                    )

                    SummaryRowWithPainter(
                        painterRes = Res.drawable.ic_ruller,
                        label = stringResource(Res.string.onboarding_summary_label_height),
                        value = stringResource(Res.string.onboarding_summary_value_height, userData.height)
                    )

                    if (userData.goals.isNotEmpty()) {
                        SummaryRowWithPainter(
                            painterRes = Res.drawable.ic_medal,
                            label = stringResource(Res.string.onboarding_summary_label_goals),
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
fun SummaryRowWithVector(
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

@Composable
fun SummaryRowWithPainter(
    painterRes: org.jetbrains.compose.resources.DrawableResource,
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
                painter = painterResource(painterRes),
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

@Preview
@Composable
fun OnboardSummaryScreenPreview() {
    EvoFitTheme {
        OnboardSummaryContent(
            userData = OnboardingUiState(
                name = "Wesley",
                birthDate = "27/07/1995",
                weight = "78",
                height = "175",
                goals = emptyList()
            ),
            currentPage = 5,
            totalPages = 6,
            onStartTraining = {},
            onBack = {}
        )
    }
}
