package com.guaracode.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitButton
import com.guaracode.evofit.presentation.ui.feature.onboard.components.OnboardingPage
import com.guaracode.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.koinInject

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    currentPage: Int,
    totalPages: Int,
    tracker: OnboardingTracker = koinInject()
) {
    LaunchedEffect(Unit) {
        tracker.trackOnboardingWelcomeScreenView()
    }

    val welcomePage = OnboardingPage(
        title = "Bem-vindo ao",
        highlightText = "EvoFit",
        description = "Treinos personalizados, evolução real e resultados de verdade."
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
    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
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
                    text = "Começar",
                    onClick = onFinish
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OnboardingPageContent(page = page)
        }
    }
}

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.OnboardingLogoSize)
                .clip(RoundedCornerShape(Dimens.CornerRadiusDefault))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            // Logo icon placeholder
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingExtraExtraLarge))

        Text(
            text = page.title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = page.highlightText,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        Text(
            text = page.description,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
