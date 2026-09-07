package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.presentation.ui.feature.authentication.components.ForgotPasswordFooter
import com.example.evofit.presentation.ui.feature.authentication.components.ForgotPasswordHeader
import com.example.evofit.presentation.ui.feature.authentication.components.ForgotPasswordIllustration
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * Success screen of the password recovery flow.
 * Informs the user that the reset link has been sent to their e-mail.
 */
@Composable
fun ForgotPasswordScreen(
    email: String,
    onContinueClick: () -> Unit = {}
) {
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
            
            // Add a small spacer to avoid clipping if scroll is active
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    EvoFitTheme {
        ForgotPasswordContent(
            email = "exemplo@email.com",
            onContinueClick = {}
        )
    }
}
