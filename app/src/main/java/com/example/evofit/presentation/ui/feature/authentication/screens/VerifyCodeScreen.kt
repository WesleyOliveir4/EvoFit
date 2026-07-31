package com.example.evofit.presentation.ui.feature.authentication.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.OtpCodeInput
import com.example.evofit.presentation.ui.feature.authentication.components.VERIFY_CODE_LENGTH
import com.example.evofit.presentation.ui.feature.authentication.components.VerifyCodeFooter
import com.example.evofit.presentation.ui.feature.authentication.components.VerifyCodeHeader
import com.example.evofit.presentation.ui.feature.authentication.components.VerifyCodeResendRow
import com.example.evofit.presentation.ui.feature.authentication.state.VerifyCodeUiState
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.VerifyCodeViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun VerifyCodeScreen(
    email: String,
    viewModel: VerifyCodeViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onCodeVerified: (oobCode: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onCodeVerified(uiState.oobCode ?: "")
            viewModel.resetSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    VerifyCodeContent(
        email = email,
        uiState = uiState,
        onCodeChange = viewModel::onCodeChange,
        onBackClick = onBackClick,
        onVerifyClick = { viewModel.onVerifyClick(email) },
        onResendClick = { viewModel.onResendClick(email) }
    )
}

@Composable
fun VerifyCodeContent(
    email: String,
    uiState: VerifyCodeUiState,
    onCodeChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        containerColor = AppDarkBg,
        topBar = {
            TopBarReturn(
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingExtraLarge),
                contentAlignment = Alignment.Center
            ) {
                VerifyCodeFooter(
                    onVerifyClick = onVerifyClick,
                    enabled = uiState.code.length == VERIFY_CODE_LENGTH,
                    isLoading = uiState.isLoading
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Column {
                VerifyCodeHeader(email = email)

                Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

                OtpCodeInput(
                    code = uiState.code,
                    onCodeChange = onCodeChange,
                    enabled = !uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingExtraLarge))

            VerifyCodeResendRow(
                secondsRemaining = uiState.secondsRemaining,
                onResendClick = onResendClick
            )
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifyCodeScreenPreview() {
    EvoFitTheme {
        VerifyCodeContent(
            email = "seu@email.com",
            uiState = VerifyCodeUiState(code = "12"),
            onCodeChange = {},
            onBackClick = {},
            onVerifyClick = {},
            onResendClick = {}
        )
    }
}
