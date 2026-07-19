package com.example.evofit.presentation.ui.feature.authentication.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.RegisterFooter
import com.example.evofit.presentation.ui.feature.authentication.components.RegisterHeader
import com.example.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.example.evofit.presentation.ui.feature.authentication.components.TermsCheckboxRow
import com.example.evofit.presentation.ui.feature.authentication.state.RegisterUiState
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.RegisterViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    onTermsOfUseClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val passwordsMismatch = uiState.confirmPassword.isNotEmpty() && uiState.confirmPassword != uiState.password
    val canSubmit = uiState.email.isNotBlank() &&
        uiState.password.isNotBlank() &&
        !passwordsMismatch &&
        uiState.termsAccepted &&
        !uiState.isLoading

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, context.getString(R.string.register_success), Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
            viewModel.resetSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
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
        onTermsOfUseClick = onTermsOfUseClick,
        onPrivacyPolicyClick = onPrivacyPolicyClick,
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
        modifier = modifier,
        containerColor = AppDarkBg,
        topBar = {
            TopBarReturn(
                onBackClick = onLoginClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- BLOCO SUPERIOR: Boas-vindas ---
            RegisterHeader()

            // --- BLOCO CENTRAL: Formulário ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo de E-mail
                LoginInputField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = stringResource(id = R.string.login_label_email),
                    placeholder = stringResource(id = R.string.login_placeholder_email),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading
                )

                // Campo de Senha
                LoginInputField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = stringResource(id = R.string.login_label_password),
                    placeholder = stringResource(id = R.string.login_placeholder_password),
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (uiState.isPasswordVisible) {
                                    stringResource(id = R.string.login_content_desc_hide_password)
                                } else {
                                    stringResource(id = R.string.login_content_desc_show_password)
                                },
                                tint = TextSecondary
                            )
                        }
                    },
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading
                )

                // Campo de Confirmar Senha
                LoginInputField(
                    value = uiState.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = stringResource(id = R.string.register_label_confirm_password),
                    placeholder = stringResource(id = R.string.register_placeholder_confirm_password),
                    trailingIcon = {
                        IconButton(onClick = onToggleConfirmPasswordVisibility) {
                            Icon(
                                imageVector = if (uiState.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (uiState.isConfirmPasswordVisible) {
                                    stringResource(id = R.string.login_content_desc_hide_password)
                                } else {
                                    stringResource(id = R.string.login_content_desc_show_password)
                                },
                                tint = TextSecondary
                            )
                        }
                    },
                    visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading
                )

                if (passwordsMismatch) {
                    Text(
                        text = stringResource(id = R.string.register_error_passwords_dont_match),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
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

            // --- BLOCO INFERIOR: Ações e Login ---
            RegisterFooter(
                isLoading = uiState.isLoading,
                enabled = canSubmit,
                onRegisterClick = onRegisterClick,
                onLoginClick = onLoginClick
            )
        }
    }
}

@Preview(showBackground = true)
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
