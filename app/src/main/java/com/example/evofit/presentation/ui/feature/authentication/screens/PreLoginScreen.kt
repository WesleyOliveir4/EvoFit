package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.presentation.ui.feature.authentication.components.PreLoginFooter
import com.example.evofit.presentation.ui.feature.authentication.components.PreLoginHeader
import com.example.evofit.presentation.ui.feature.authentication.components.PreLoginPageIndicator
import com.example.evofit.presentation.ui.theme.AppDarkBg
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
        modifier = modifier,
        containerColor = AppDarkBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                PreLoginHeader()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                PreLoginPageIndicator()
                PreLoginFooter(onStartClick = onStartClick)
            }
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
