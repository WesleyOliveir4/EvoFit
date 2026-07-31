package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.evofit.presentation.ui.feature.components.EvoDatePickerDialog
import com.example.evofit.presentation.ui.feature.components.EvoFitButton
import com.example.evofit.presentation.ui.feature.components.EvoFitInputField
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.feature.onboard.state.OnboardingUiState
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardUserDataScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val userData by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardUserDataContent(
        userData = userData,
        currentPage = currentPage,
        totalPages = totalPages,
        onNameChange = { viewModel.updateProfile(name = it) },
        onBirthDateChange = { viewModel.updateProfile(birthDate = it) },
        onContinue = { viewModel.saveAndNext(onContinue) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardUserDataContent(
    userData: OnboardingUiState,
    currentPage: Int,
    totalPages: Int,
    onNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val isFormValid by remember(userData.name, userData.birthDate) {
        derivedStateOf {
            userData.name.isNotBlank() && userData.birthDate.isNotBlank()
        }
    }

    if (showDatePicker) {
        EvoDatePickerDialog(
            initialDate = userData.birthDate,
            onDismiss = { showDatePicker = false },
            onConfirm = onBirthDateChange
        )
    }

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
                    enabled = isFormValid,
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
                text = stringResource(R.string.onboarding_user_data_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = stringResource(R.string.onboarding_user_data_description),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            EvoFitInputField(
                label = stringResource(R.string.onboarding_user_data_label_name),
                placeholder = stringResource(R.string.onboarding_user_data_label_name_example),
                value = userData.name,
                onValueChange = onNameChange
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            EvoFitInputField(
                label = stringResource(R.string.onboarding_user_data_label_birth_date),
                placeholder = stringResource(
                    R.string.onboarding_user_data_label_birth_date_example
                ),
                value = userData.birthDate,
                enabled = false,
                onValueChange = {},
                modifier = Modifier.clickable { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
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
                birthDate = "25/05/1999",
                goals = emptyList()
            ),
            currentPage = 1,
            totalPages = 6,
            onNameChange = {},
            onBirthDateChange = {},
            onContinue = {},
            onBack = {}
        )
    }
}
