package com.guaracode.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.guaracode.evofit.presentation.ui.feature.authentication.components.RegisterFooter
import com.guaracode.evofit.presentation.ui.feature.authentication.components.RegisterHeader
import com.guaracode.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.guaracode.evofit.presentation.ui.feature.authentication.components.TermsCheckboxRow
import com.guaracode.evofit.presentation.ui.feature.authentication.state.RegisterUiState
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel.RegisterViewModel
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    authTracker: AuthTracker = koinInject(),
    onBackClick: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    onTermsOfUseClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val passwordsMismatch = uiState.confirmPassword.isNotEmpty() && uiState.confirmPassword != uiState.password
    val canSubmit = uiState.email.isNotBlank() &&
        uiState.password.isNotBlank() &&
        !passwordsMismatch &&
        uiState.termsAccepted &&
        !uiState.isLoading

    LaunchedEffect(Unit) {
        authTracker.trackRegisterScreenView()
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess()
            viewModel.resetSuccess()
        }
    }

    RegisterContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onToggleConfirmPasswordVisibility = viewModel::onToggleConfirmPasswordVisibility,
        passwordsMismatch = passwordsMismatch,
        onTermsAcceptedChange = viewModel::onTermsAcceptedChange,
        onTermsOfUseClick = {
            authTracker.trackTermsClicked()
            onTermsOfUseClick()
        },
        onPrivacyPolicyClick = {
            authTracker.trackTermsClicked()
            onPrivacyPolicyClick()
        },
        canSubmit = canSubmit,
        onRegisterClick = viewModel::onRegisterClick,
        onLoginClick = onBackClick
    )
}

@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    passwordsMismatch: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onTermsOfUseClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    canSubmit: Boolean,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarReturn(
                onBackClick = onLoginClick
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingMedium),
                contentAlignment = Alignment.Center
            ) {
                RegisterFooter(
                    isLoading = uiState.isLoading,
                    enabled = canSubmit,
                    onRegisterClick = onRegisterClick,
                    onLoginClick = onLoginClick
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
            horizontalAlignment = Alignment.Start
        ) {
            RegisterHeader()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.SectionSpacing),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
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

                LoginInputField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = "Senha",
                    placeholder = "Digite sua senha cadastrada",
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Text(
                                text = if (uiState.isPasswordVisible) "🙈" else "👁",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading
                )

                LoginInputField(
                    value = uiState.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = "Confirmar Senha",
                    placeholder = "Confirme sua senha",
                    trailingIcon = {
                        IconButton(onClick = onToggleConfirmPasswordVisibility) {
                            Text(
                                text = if (uiState.isConfirmPasswordVisible) "🙈" else "👁",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading
                )

                if (passwordsMismatch) {
                    Text(
                        text = "As senhas não coincidem.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                TermsCheckboxRow(
                    checked = uiState.termsAccepted,
                    onCheckedChange = onTermsAcceptedChange,
                    onTermsOfUseClick = onTermsOfUseClick,
                    onPrivacyPolicyClick = onPrivacyPolicyClick,
                    enabled = !uiState.isLoading
                )
            }
        }
    }
}
