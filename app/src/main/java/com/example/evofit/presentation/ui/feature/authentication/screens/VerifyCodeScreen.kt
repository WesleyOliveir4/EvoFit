package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.AuthBackButton
import com.example.evofit.presentation.ui.feature.authentication.components.OtpCodeInput
import com.example.evofit.presentation.ui.feature.authentication.components.VERIFY_CODE_LENGTH
import com.example.evofit.presentation.ui.feature.authentication.components.VerifyCodeFooter
import com.example.evofit.presentation.ui.feature.authentication.components.VerifyCodeHeader
import com.example.evofit.presentation.ui.feature.authentication.components.VerifyCodeResendRow
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import kotlinx.coroutines.delay

private const val RESEND_COOLDOWN_SECONDS = 45

/**
 * "Verifique seu email" screen (mock screen 6). Receives the e-mail typed in
 * [RecoverPasswordScreen] and collects the 6-digit code. The countdown/resend
 * timer is local UI state; the actual code dispatch/validation
 * (VerifyPasswordResetCodeUseCase + Firebase) is wired in a later step.
 */
@Composable
fun VerifyCodeScreen(
    email: String,
    onBackClick: () -> Unit = {},
    onVerifyClick: (code: String) -> Unit = {},
    onResendClick: () -> Unit = {}
) {
    var code by remember { mutableStateOf("") }
    var secondsRemaining by remember { mutableIntStateOf(RESEND_COOLDOWN_SECONDS) }

    LaunchedEffect(secondsRemaining) {
        if (secondsRemaining > 0) {
            delay(1_000)
            secondsRemaining -= 1
        }
    }

    VerifyCodeContent(
        email = email,
        code = code,
        onCodeChange = { code = it },
        secondsRemaining = secondsRemaining,
        onBackClick = onBackClick,
        onVerifyClick = { onVerifyClick(code) },
        onResendClick = {
            secondsRemaining = RESEND_COOLDOWN_SECONDS
            onResendClick()
        }
    )
}

@Composable
fun VerifyCodeContent(
    email: String,
    code: String,
    onCodeChange: (String) -> Unit,
    secondsRemaining: Int,
    onBackClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
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
            Column {
                AuthBackButton(
                    onBackClick = onBackClick,
                    contentDescription = stringResource(id = R.string.verify_code_back_desc),
                    modifier = Modifier.padding(top = 16.dp)
                )

                VerifyCodeHeader(email = email)

                Spacer(modifier = Modifier.height(32.dp))

                OtpCodeInput(
                    code = code,
                    onCodeChange = onCodeChange,
                    enabled = !isLoading
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                VerifyCodeResendRow(
                    secondsRemaining = secondsRemaining,
                    onResendClick = onResendClick
                )
                VerifyCodeFooter(
                    onVerifyClick = onVerifyClick,
                    enabled = code.length == VERIFY_CODE_LENGTH,
                    isLoading = isLoading
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifyCodeScreenPreview() {
    EvoFitTheme {
        VerifyCodeContent(
            email = "seu@email.com",
            code = "12",
            onCodeChange = {},
            secondsRemaining = 45,
            onBackClick = {},
            onVerifyClick = {},
            onResendClick = {}
        )
    }
}
