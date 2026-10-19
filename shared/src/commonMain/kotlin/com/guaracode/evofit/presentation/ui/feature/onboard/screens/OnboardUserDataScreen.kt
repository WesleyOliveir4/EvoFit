package com.guaracode.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.guaracode.evofit.presentation.ui.feature.components.EvoDatePickerDialog
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitButton
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitInputField
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.guaracode.evofit.presentation.ui.feature.onboard.state.OnboardingUiState
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import com.guaracode.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_today
import evofit.shared.generated.resources.onboarding_button_continue
import evofit.shared.generated.resources.onboarding_user_data_description
import evofit.shared.generated.resources.onboarding_user_data_label_birth_date
import evofit.shared.generated.resources.onboarding_user_data_label_birth_date_example
import evofit.shared.generated.resources.onboarding_user_data_label_name
import evofit.shared.generated.resources.onboarding_user_data_label_name_example
import evofit.shared.generated.resources.onboarding_user_data_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardUserDataScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
    tracker: OnboardingTracker = koinInject()
) {
    val userData by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        tracker.trackOnboardingUserDataScreenView()
    }

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
                    text = stringResource(Res.string.onboarding_button_continue),
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
                text = stringResource(Res.string.onboarding_user_data_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = stringResource(Res.string.onboarding_user_data_description),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            EvoFitInputField(
                label = stringResource(Res.string.onboarding_user_data_label_name),
                placeholder = stringResource(Res.string.onboarding_user_data_label_name_example),
                value = userData.name,
                onValueChange = onNameChange
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            EvoFitInputField(
                label = stringResource(Res.string.onboarding_user_data_label_birth_date),
                placeholder = stringResource(Res.string.onboarding_user_data_label_birth_date_example),
                value = userData.birthDate,
                enabled = false,
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_today),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                    )
                },
                onValueChange = {},
                modifier = Modifier.clickable { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}

@Preview
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
