package com.guaracode.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.guaracode.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.guaracode.evofit.presentation.ui.feature.authentication.components.RecoverPasswordFooter
import com.guaracode.evofit.presentation.ui.feature.authentication.components.RecoverPasswordHeader
import com.guaracode.evofit.presentation.ui.feature.authentication.state.RecoverPasswordUiState
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel.RecoverPasswordViewModel
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel = koinViewModel(),
    authTracker: AuthTracker = koinInject(),
    onBackClick: () -> Unit = {},
    onCodeSent: (email: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    val isEmailValid = emailRegex.matches(uiState.email)

    LaunchedEffect(Unit) {
        authTracker.trackRecoverPasswordScreenView()
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onCodeSent(uiState.email)
            viewModel.resetSuccess()
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

                if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = Dimens.SpacingSmall)
                    )
                }

                LoginInputField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = "E-mail",
                    placeholder = "Seu e-mail cadastrado",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading
                )
            }
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}
