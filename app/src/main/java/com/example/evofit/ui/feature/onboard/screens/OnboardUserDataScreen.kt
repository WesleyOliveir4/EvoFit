package com.example.evofit.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.ui.feature.onboard.components.OnboardingButton
import com.example.evofit.ui.feature.onboard.components.PageIndicators
import com.example.evofit.ui.feature.onboard.components.UserInputField

@Composable
fun OnboardUserDataScreen(
    name: String,
    age: String,
    weight: String,
    currentPage: Int,
    totalPages: Int,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onContinue: () -> Unit
) {
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
            value = name,
            onValueChange = onNameChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(
            label = "Idade",
            value = age,
            keyboardType = KeyboardType.Number,
            onValueChange = onAgeChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(
            label = "Peso (kg)",
            value = weight,
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
            onClick = onContinue
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
