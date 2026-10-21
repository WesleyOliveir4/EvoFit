package com.guaracode.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.guaracode.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.guaracode.evofit.presentation.ui.feature.authentication.components.RegisterFooter
import com.guaracode.evofit.presentation.ui.feature.authentication.components.RegisterHeader
import com.guaracode.evofit.presentation.ui.feature.authentication.components.TermsCheckboxRow
import com.guaracode.evofit.presentation.ui.feature.authentication.state.RegisterUiState
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel.RegisterViewModel
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.login_content_desc_hide_password
import evofit.shared.generated.resources.login_content_desc_show_password
import evofit.shared.generated.resources.login_label_email
import evofit.shared.generated.resources.login_label_password
import evofit.shared.generated.resources.login_placeholder_email
import evofit.shared.generated.resources.login_placeholder_password
import evofit.shared.generated.resources.register_error_passwords_dont_match
import evofit.shared.generated.resources.register_label_confirm_password
import evofit.shared.generated.resources.register_placeholder_confirm_password
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
                LoginInputField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = stringResource(Res.string.login_label_email),
                    placeholder = stringResource(Res.string.login_placeholder_email),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading
                )

                LoginInputField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = stringResource(Res.string.login_label_password),
                    placeholder = stringResource(Res.string.login_placeholder_password),
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (uiState.isPasswordVisible) {
                                    stringResource(Res.string.login_content_desc_hide_password)
                                } else {
                                    stringResource(Res.string.login_content_desc_show_password)
                                },
                                tint = MaterialTheme.colorScheme.secondary
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
                    label = stringResource(Res.string.register_label_confirm_password),
                    placeholder = stringResource(Res.string.register_placeholder_confirm_password),
                    trailingIcon = {
                        IconButton(onClick = onToggleConfirmPasswordVisibility) {
                            Icon(
                                imageVector = if (uiState.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (uiState.isConfirmPasswordVisible) {
                                    stringResource(Res.string.login_content_desc_hide_password)
                                } else {
                                    stringResource(Res.string.login_content_desc_show_password)
                                },
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    },
                    visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading
                )

                if (passwordsMismatch) {
                    Text(
                        text = stringResource(Res.string.register_error_passwords_dont_match),
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

@Preview
@Composable
private fun RegisterScreenPreview() {
    EvoFitTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onConfirmPasswordChange = {},
            onToggleConfirmPasswordVisibility = {},
            passwordsMismatch = false,
            onTermsAcceptedChange = {},
            onTermsOfUseClick = {},
            onPrivacyPolicyClick = {},
            canSubmit = false,
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
