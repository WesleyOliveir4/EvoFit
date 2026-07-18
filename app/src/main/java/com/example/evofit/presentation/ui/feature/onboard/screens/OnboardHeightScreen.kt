package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.onboard.components.EvoWheelPicker
import com.example.evofit.presentation.ui.feature.onboard.components.OnboardingButton
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardHeightScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
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
        onContinue = { viewModel.saveAndNext(onContinue) }
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
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.onboarding_height_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.onboarding_height_description),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 18.sp
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

        PageIndicators(
            pageCount = totalPages,
            selectedPage = currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        OnboardingButton(
            text = stringResource(R.string.onboarding_button_continue),
            enabled = isButtonEnabled,
            onClick = onContinue
        )

        Spacer(modifier = Modifier.height(32.dp))
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
            onContinue = {}
        )
    }
}
