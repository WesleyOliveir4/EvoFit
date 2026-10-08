package com.guaracode.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.guaracode.evofit.R
import com.guaracode.evofit.presentation.ui.feature.authentication.components.PreLoginFooter
import com.guaracode.evofit.presentation.ui.feature.authentication.components.PreLoginHeader
import com.guaracode.evofit.presentation.ui.feature.authentication.components.PreLoginPageIndicator
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitButton
import com.guaracode.evofit.presentation.ui.theme.AppDarkBg
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.compose.koinInject

/**
 * First screen of the authentication flow (mock screen 1 - "Bem-vindo(a)").
 * Purely presentational: no ViewModel/business rule is required here, it only
 * kicks off the flow by navigating to [com.guaracode.evofit.presentation.ui.feature.authentication.screens.LoginScreen].
 */
@Composable
fun PreLoginScreen(
    authTracker: AuthTracker = koinInject(),
    onStartClick: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        authTracker.trackPreLoginScreenView()
    }

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
