package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.presentation.ui.feature.onboard.components.OnboardingButton
import com.example.evofit.presentation.ui.feature.onboard.components.OnboardingPage
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    currentPage: Int,
    totalPages: Int
) {
    val welcomePage =
        OnboardingPage(
            title = "Bem-vindo ao",
            highlightText = "EvoFit 💪",
            description = "Vamos configurar seu perfil para acompanhar sua evolução."
        )

    OnboardingContent(
        page = welcomePage,
        currentPage = currentPage,
        totalPages = totalPages,
        onFinish = onFinish
    )
}

@Composable
fun OnboardingContent(
    page: OnboardingPage,
    currentPage: Int,
    totalPages: Int,
    onFinish: () -> Unit
) {
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
            page = page,
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

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF102312))
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = page.highlightText,
            color = Color(0xFF67D14E),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            color = Color(0xFFBDBDBD),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingContent(
        page = OnboardingPage(
            title = "Bem-vindo ao",
            highlightText = "EvoFit 💪",
            description = "Vamos configurar seu perfil para acompanhar sua evolução."
        ),
        currentPage = 0,
        totalPages = 3,
        onFinish = {}
    )
}
