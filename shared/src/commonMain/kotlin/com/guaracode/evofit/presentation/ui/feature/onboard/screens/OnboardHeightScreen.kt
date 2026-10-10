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
fun OnboardHeightScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
    tracker: OnboardingTracker = koinInject()
) {
    val userData by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        tracker.trackOnboardingHeightScreenView()
    }
    
    val heightRange = remember { (100..250).toList() }
    val initialHeight = remember(userData.height) {
        userData.height.toIntOrNull() ?: 170
    }

    val isButtonEnabled by remember(userData.height) {
        derivedStateOf { userData.height.isNotBlank() }
    }

    OnboardHeightContent(
        height = initialHeight,
        heightRange = heightRange,
        currentPage = currentPage,
        totalPages = totalPages,
        isButtonEnabled = isButtonEnabled,
        onHeightChange = { viewModel.updateProfile(height = it.toString()) },
        onContinue = { viewModel.saveAndNext(onContinue) },
        onBack = onBack
    )
}

@Composable
fun OnboardHeightContent(
    height: Int,
    heightRange: List<Int>,
    currentPage: Int,
    totalPages: Int,
    isButtonEnabled: Boolean,
    onHeightChange: (Int) -> Unit,
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
                text = "Qual é a sua altura?",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = "Usamos sua altura para métricas de composição corporal.",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(0.5f))

            EvoWheelPicker(
                range = heightRange,
                unit = "cm",
                initialValue = height,
                onValueChange = onHeightChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}
