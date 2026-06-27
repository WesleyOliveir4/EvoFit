package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.presentation.ui.feature.onboard.components.OnboardingButton
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
    val userData by viewModel.userData.collectAsStateWithLifecycle()

    OnboardUserDataContent(
        userData = userData,
        currentPage = currentPage,
        totalPages = totalPages,
        onNameChange = remember { { viewModel.updateProfile(name = it) } },
        onAgeChange = remember { { viewModel.updateProfile(age = it) } },
        onWeightChange = remember { { viewModel.updateProfile(weight = it) } },
        onHeightChange = remember { { viewModel.updateProfile(height = it) } },
        onContinue = remember { { viewModel.saveAndNext(onContinue) } }
    )
}

@Composable
fun OnboardUserDataContent(
    userData: UserOnboardingData,
    currentPage: Int,
    totalPages: Int,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    val isFormValid by remember(userData) {
        derivedStateOf {
            userData.name.isNotBlank() &&
                    userData.age.isNotBlank() &&
                    userData.weight.isNotBlank() &&
                    userData.height.isNotBlank()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(
            label = stringResource(R.string.onboarding_user_data_label_weight),
            value = userData.weight,
            keyboardType = KeyboardType.Number,
            onValueChange = onWeightChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(
            label = stringResource(R.string.onboarding_user_data_label_height),
            value = userData.height,
            keyboardType = KeyboardType.Number,
            onValueChange = onHeightChange
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
            enabled = isFormValid,
            onClick = onContinue
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardUserDataScreenPreview() {
    EvoFitTheme {
        OnboardUserDataContent(
            userData = UserOnboardingData(
                name = "João",
                age = "25",
                weight = "80",
                height = "175",
                goals = emptyList()
            ),
            currentPage = 1,
            totalPages = 4,
            onNameChange = {},
            onAgeChange = {},
            onWeightChange = {},
            onHeightChange = {},
            onContinue = {}
        )
    }
}
