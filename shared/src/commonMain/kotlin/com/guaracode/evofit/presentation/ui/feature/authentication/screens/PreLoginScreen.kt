package com.guaracode.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.guaracode.evofit.presentation.ui.feature.authentication.components.PreLoginFooter
import com.guaracode.evofit.presentation.ui.feature.authentication.components.PreLoginHeader
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.theme.AppDarkBg
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.koinInject

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
                PreLoginFooter(
                    onStartClick = onStartClick
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
