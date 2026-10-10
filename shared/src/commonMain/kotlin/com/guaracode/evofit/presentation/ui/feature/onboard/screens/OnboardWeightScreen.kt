package com.guaracode.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitButton
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.onboard.components.EvoWheelPicker
import com.guaracode.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import com.guaracode.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject

@Composable
fun OnboardWeightScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
    tracker: OnboardingTracker = koinInject()
) {
    val userData by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        tracker.trackOnboardingWeightScreenView()
    }
    
    val weightRange = remember { (30..200).toList() }
    val initialWeight = remember(userData.weight) {
        userData.weight.toIntOrNull() ?: 70
    }

    val isButtonEnabled by remember(userData.weight) {
        derivedStateOf { userData.weight.isNotBlank() }
    }

    OnboardWeightContent(
        weight = initialWeight,
        weightRange = weightRange,
        currentPage = currentPage,
        totalPages = totalPages,
        isButtonEnabled = isButtonEnabled,
        onWeightChange = { viewModel.updateProfile(weight = it.toString()) },
        onContinue = { viewModel.saveAndNext(onContinue) },
        onBack = onBack
    )
}

@Composable
fun OnboardWeightContent(
    weight: Int,
    weightRange: List<Int>,
    currentPage: Int,
    totalPages: Int,
    isButtonEnabled: Boolean,
    onWeightChange: (Int) -> Unit,
    onContinue: () -> Unit,
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
                    text = "Continuar",
                    enabled = isButtonEnabled,
                    onClick = onContinue
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
        ) {
            Text(
                text = "Qual é o seu peso atual?",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = "Isso nos ajuda a calcular seu gasto calórico e acompanhar sua evolução.",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(0.5f))

            EvoWheelPicker(
                range = weightRange,
                unit = "kg",
                initialValue = weight,
                onValueChange = onWeightChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}
