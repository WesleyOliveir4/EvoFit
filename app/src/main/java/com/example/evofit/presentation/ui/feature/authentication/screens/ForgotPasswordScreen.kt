package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.presentation.ui.feature.authentication.components.ForgotPasswordFooter
import com.example.evofit.presentation.ui.feature.authentication.components.ForgotPasswordHeader
import com.example.evofit.presentation.ui.feature.authentication.components.ForgotPasswordIllustration
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * Intro screen of the "esqueci minha senha" sub-flow (mock screen 4).
 * Purely presentational — it only explains what is about to happen and
 * hands off to [RecoverPasswordScreen] where the user types their e-mail.
 */
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    ForgotPasswordContent(
        onBackClick = onBackClick,
        onContinueClick = onContinueClick
    )
}

@Composable
fun ForgotPasswordContent(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        containerColor = AppDarkBg,
        topBar = {
            TopBarReturn(
                onBackClick = { onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ForgotPasswordIllustration()
                Spacer(modifier = Modifier.height(32.dp))
                ForgotPasswordHeader()
            }

            ForgotPasswordFooter(
                onContinueClick = onContinueClick,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    EvoFitTheme {
        ForgotPasswordContent(
            onBackClick = {},
            onContinueClick = {}
        )
    }
}
