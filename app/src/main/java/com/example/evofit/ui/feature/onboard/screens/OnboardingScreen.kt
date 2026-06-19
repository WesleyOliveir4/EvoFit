package com.example.evofit.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.evofit.ui.feature.onboard.components.OnboardingButton
import com.example.evofit.ui.feature.onboard.components.OnboardingPage
import com.example.evofit.ui.feature.onboard.components.OnboardingPageContent
import com.example.evofit.ui.feature.onboard.components.PageIndicators

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    currentPage: Int,
    totalPages: Int
) {
    val welcomePage = OnboardingPage(
        title = "Bem-vindo ao",
        highlightText = "EvoFit 💪",
        description = "Vamos configurar seu perfil para acompanhar sua evolução."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        OnboardingPageContent(
            page = welcomePage,
            modifier = Modifier.weight(8f)
        )

        PageIndicators(
            pageCount = totalPages,
            selectedPage = currentPage,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OnboardingButton(
            text = "Começar",
            onClick = onFinish
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
