package com.guaracode.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun OnboardUserDataScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
    tracker: OnboardingTracker = koinInject()
) {
    val userData by viewModel.uiState.collectAsStateWithLifecycle()

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
    val isFormValid by remember(userData.name, userData.birthDate) {
        derivedStateOf {
            userData.name.isNotBlank() && userData.birthDate.isNotBlank()
        }
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
                    text = "Continuar",
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
                text = "Seus dados pessoais",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = "Precisamos dessas informações para personalizar sua experiência.",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            OutlinedTextField(
                value = userData.name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome") },
                placeholder = { Text("Seu nome completo") },
                singleLine = true,
                shape = RoundedCornerShape(Dimens.CornerRadiusExtraSmall)
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            OutlinedTextField(
                value = userData.birthDate,
                onValueChange = onBirthDateChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Data de nascimento") },
                placeholder = { Text("DD/MM/AAAA") },
                singleLine = true,
                shape = RoundedCornerShape(Dimens.CornerRadiusExtraSmall)
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}
