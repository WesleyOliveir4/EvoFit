package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.components.EvoFitButton
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.feature.onboard.components.EvoWheelPicker
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardHeightScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val userData by viewModel.uiState.collectAsStateWithLifecycle()
    
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

@OptIn(ExperimentalMaterial3Api::class)
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
                    text = stringResource(R.string.onboarding_button_continue),
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
                text = stringResource(R.string.onboarding_height_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = stringResource(R.string.onboarding_height_description),
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

@Preview(showBackground = true)
@Composable
fun OnboardHeightScreenPreview() {
    EvoFitTheme {
        OnboardHeightContent(
            height = 175,
            heightRange = (100..250).toList(),
            currentPage = 3,
            totalPages = 6,
            isButtonEnabled = true,
            onHeightChange = {},
            onContinue = {},
            onBack = {}
        )
    }
}
