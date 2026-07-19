package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.presentation.ui.feature.components.EvoFitButton
import com.example.evofit.presentation.ui.feature.onboard.state.OnboardingUiState
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.feature.onboard.components.UserInputField
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardUserDataScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val userData by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardUserDataContent(
        userData = userData,
        currentPage = currentPage,
        totalPages = totalPages,
        onNameChange = { viewModel.updateProfile(name = it) },
        onAgeChange = { viewModel.updateProfile(age = it) },
        onContinue = { viewModel.saveAndNext(onContinue) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardUserDataContent(
    userData: OnboardingUiState,
    currentPage: Int,
    totalPages: Int,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    val isFormValid by remember(userData.name, userData.age) {
        derivedStateOf {
            userData.name.isNotBlank() && userData.age.isNotBlank()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.onboarding_user_data_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.onboarding_user_data_description),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            UserInputField(
                label = stringResource(R.string.onboarding_user_data_label_name),
                value = userData.name,
                onValueChange = onNameChange
            )

            Spacer(modifier = Modifier.height(20.dp))

            UserInputField(
                label = stringResource(R.string.onboarding_user_data_label_age),
                value = userData.age,
                keyboardType = KeyboardType.Number,
                onValueChange = onAgeChange
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
                enabled = isFormValid,
                onClick = onContinue
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardUserDataScreenPreview() {
    EvoFitTheme {
        OnboardUserDataContent(
            userData = OnboardingUiState(
                name = "João",
                age = "25",
                goals = emptyList()
            ),
            currentPage = 1,
            totalPages = 6,
            onNameChange = {},
            onAgeChange = {},
            onContinue = {}
        )
    }
}
