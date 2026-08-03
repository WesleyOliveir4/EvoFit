package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.PreLoginFooter
import com.example.evofit.presentation.ui.feature.authentication.components.PreLoginHeader
import com.example.evofit.presentation.ui.feature.authentication.components.PreLoginPageIndicator
import com.example.evofit.presentation.ui.feature.components.EvoFitButton
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * First screen of the authentication flow (mock screen 1 - "Bem-vindo(a)").
 * Purely presentational: no ViewModel/business rule is required here, it only
 * kicks off the flow by navigating to [com.example.evofit.presentation.ui.feature.authentication.screens.LoginScreen].
 */
@Composable
fun PreLoginScreen(
    onStartClick: () -> Unit = {}
) {
    PreLoginContent(onStartClick = onStartClick)
}

@Composable
fun PreLoginContent(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        containerColor = AppDarkBg,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingExtraLarge),
                contentAlignment = Alignment.Center
            ) {
                EvoFitButton(
                    text = stringResource(R.string.pre_login_button_start),
                    onClick = onStartClick
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
            PreLoginHeader()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreLoginScreenPreview() {
    EvoFitTheme {
        PreLoginContent(onStartClick = {})
    }
}
