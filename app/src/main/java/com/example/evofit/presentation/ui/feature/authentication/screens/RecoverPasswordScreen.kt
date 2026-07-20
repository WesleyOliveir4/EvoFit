package com.example.evofit.presentation.ui.feature.authentication.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.example.evofit.presentation.ui.feature.authentication.components.RecoverPasswordFooter
import com.example.evofit.presentation.ui.feature.authentication.components.RecoverPasswordHeader
import com.example.evofit.presentation.ui.feature.authentication.state.RecoverPasswordUiState
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.RecoverPasswordViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextSecondary
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onCodeSent: (email: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onCodeSent(uiState.email)
            viewModel.resetSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    RecoverPasswordContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        canSubmit = isEmailValid,
        onBackClick = onBackClick,
        onSendCodeClick = viewModel::onSendCodeClick
    )
}

@Composable
fun RecoverPasswordContent(
    uiState: RecoverPasswordUiState,
    onEmailChange: (String) -> Unit,
    canSubmit: Boolean,
    onBackClick: () -> Unit,
    onSendCodeClick: () -> Unit,
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
                RecoverPasswordHeader()

                Spacer(modifier = Modifier.height(32.dp))

                LoginInputField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = stringResource(id = R.string.login_label_email),
                    placeholder = stringResource(id = R.string.login_placeholder_email),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading
                )
            }

            RecoverPasswordFooter(
                onSendCodeClick = onSendCodeClick,
                enabled = canSubmit,
                isLoading = uiState.isLoading,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecoverPasswordScreenPreview() {
    EvoFitTheme {
        RecoverPasswordContent(
            uiState = RecoverPasswordUiState(),
            onEmailChange = {},
            canSubmit = false,
            onBackClick = {},
            onSendCodeClick = {}
        )
    }
}
