package com.example.evofit.presentation.ui.feature.authentication.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.example.evofit.presentation.ui.feature.authentication.components.RecoverPasswordFooter
import com.example.evofit.presentation.ui.feature.authentication.components.RecoverPasswordHeader
import com.example.evofit.presentation.ui.feature.authentication.state.RecoverPasswordUiState
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.RecoverPasswordViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
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
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
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
                RecoverPasswordFooter(
                    onSendCodeClick = onSendCodeClick,
                    enabled = canSubmit,
                    isLoading = uiState.isLoading
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Column {
                RecoverPasswordHeader()

                Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

                LoginInputField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = stringResource(id = R.string.login_label_email),
                    placeholder = stringResource(id = R.string.login_placeholder_email),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading
                )
            }
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
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
