package com.guaracode.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.guaracode.evofit.presentation.ui.feature.authentication.components.ForgotPasswordFooter
import com.guaracode.evofit.presentation.ui.feature.authentication.components.ForgotPasswordHeader
import com.guaracode.evofit.presentation.ui.feature.authentication.components.ForgotPasswordIllustration
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.koinInject

@Composable
fun ForgotPasswordScreen(
    email: String,
    authTracker: AuthTracker = koinInject(),
    onContinueClick: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        authTracker.trackForgotPasswordScreenView()
    }

    ForgotPasswordContent(
        email = email,
        onContinueClick = onContinueClick
    )
}

@Composable
fun ForgotPasswordContent(
    email: String,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingExtraLarge),
                contentAlignment = Alignment.Center
            ) {
                ForgotPasswordFooter(
                    onContinueClick = onContinueClick
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ForgotPasswordIllustration()
            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))
            ForgotPasswordHeader(email = email)
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}
