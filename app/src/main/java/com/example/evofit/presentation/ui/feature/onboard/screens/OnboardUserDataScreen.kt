package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.presentation.ui.feature.onboard.components.OnboardingButton
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.feature.onboard.components.UserInputField
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardUserDataScreen(
    currentPage: Int,
    totalPages: Int,
    onContinue: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val userData by viewModel.userData.collectAsState()

    OnboardUserDataContent(
        userData = userData,
        currentPage = currentPage,
        totalPages = totalPages,
        onNameChange = viewModel::updateName,
        onAgeChange = viewModel::updateAge,
        onWeightChange = viewModel::updateWeight,
        onContinue = { viewModel.saveAndNext(onContinue) }
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
    onContinue: () -> Unit
) {
    val isFormValid = userData.name.isNotBlank() && 
                      userData.age.isNotBlank() && 
                      userData.weight.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Seus dados",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Precisamos de algumas informações para personalizar sua experiência.",
            color = Color(0xFFB0BEC5),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        UserInputField(
            label = "Nome",
            value = userData.name,
            onValueChange = onNameChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(
            label = "Idade",
            value = userData.age,
            keyboardType = KeyboardType.Number,
            onValueChange = onAgeChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(
            label = "Peso (kg)",
            value = userData.weight,
            keyboardType = KeyboardType.Number,
            onValueChange = onWeightChange
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
            text = "Continuar",
            enabled = isFormValid,
            onClick = onContinue
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardUserDataScreenPreview() {
    OnboardUserDataContent(
        userData = UserOnboardingData(
            name = "João",
            age = "25",
            weight = "80",
            height = "175",
            goals = emptyList()
        ),
        currentPage = 1,
        totalPages = 3,
        onNameChange = {},
        onAgeChange = {},
        onWeightChange = {},
        onContinue = {}
    )
}
