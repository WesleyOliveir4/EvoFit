package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.components.EvoFitButton
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.feature.onboard.components.EvoWheelPicker
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardWeightScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val userData by viewModel.uiState.collectAsStateWithLifecycle()
    
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

@OptIn(ExperimentalMaterial3Api::class)
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
        topBar = {
            TopBarReturn(
                onBackClick = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp),
        ) {
            Text(
                text = stringResource(R.string.onboarding_weight_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.onboarding_weight_description),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
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

            PageIndicators(
                pageCount = totalPages,
                selectedPage = currentPage,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            EvoFitButton(
                text = stringResource(R.string.onboarding_button_continue),
                enabled = isButtonEnabled,
                onClick = onContinue
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardWeightScreenPreview() {
    EvoFitTheme {
        OnboardWeightContent(
            weight = 75,
            weightRange = (30..200).toList(),
            currentPage = 2,
            totalPages = 6,
            isButtonEnabled = true,
            onWeightChange = {},
            onContinue = {},
            onBack = {}
        )
    }
}
