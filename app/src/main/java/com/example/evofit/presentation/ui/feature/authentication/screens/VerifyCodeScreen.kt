package com.example.evofit.presentation.ui.feature.authentication.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        modifier = modifier,
        containerColor = AppDarkBg,
        topBar = {
            TopBarReturn(
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.fillMaxSize().systemBarsPadding()
                .padding(paddingValues)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                VerifyCodeHeader(email = email)

                Spacer(modifier = Modifier.height(32.dp))

                OtpCodeInput(
                    code = uiState.code,
                    onCodeChange = onCodeChange,
                    enabled = !uiState.isLoading
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                VerifyCodeResendRow(
                    secondsRemaining = uiState.secondsRemaining,
                    onResendClick = onResendClick
                )
                VerifyCodeFooter(
                    onVerifyClick = onVerifyClick,
                    enabled = uiState.code.length == VERIFY_CODE_LENGTH,
                    isLoading = uiState.isLoading
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
            uiState = VerifyCodeUiState(code = "12"),
            onCodeChange = {},
            onBackClick = {},
            onVerifyClick = {},
            onResendClick = {}
        )
    }
}
