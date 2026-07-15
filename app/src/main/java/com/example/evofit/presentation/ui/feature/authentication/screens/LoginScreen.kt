package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.LoginFooter
import com.example.evofit.presentation.ui.feature.authentication.components.LoginHeader
import com.example.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.example.evofit.presentation.ui.theme.*

import com.example.evofit.presentation.ui.feature.authentication.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: (Boolean) -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess(uiState.isOnboardingCompleted)
            viewModel.resetSuccess()
        }
    }

    LoginContent(
        email = uiState.email,
        onEmailChange = viewModel::onEmailChange,
        password = uiState.password,
        onPasswordChange = viewModel::onPasswordChange,
        isPasswordVisible = uiState.isPasswordVisible,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onLoginClick = viewModel::onLoginClick,
        onForgotPasswordClick = onForgotPasswordClick,
        onSignUpClick = onSignUpClick,
        isLoading = uiState.isLoading,
        errorMessage = uiState.error
    )
}

@Composable
fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- BLOCO SUPERIOR: Boas-vindas ---
            LoginHeader()

            // --- BLOCO CENTRAL: Formulário ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Campo de E-mail
                LoginInputField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = stringResource(id = R.string.login_label_email),
                    placeholder = stringResource(id = R.string.login_placeholder_email),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !isLoading
                )

                // Campo de Senha
                LoginInputField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = stringResource(id = R.string.login_label_password),
                    placeholder = stringResource(id = R.string.login_placeholder_password),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (isPasswordVisible) {
                                    stringResource(id = R.string.login_content_desc_hide_password)
                                } else {
                                    stringResource(id = R.string.login_content_desc_show_password)
                                },
                                tint = TextSecondary
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !isLoading
                )

                // Esqueci a Senha (Alinhado à Direita)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = stringResource(id = R.string.login_forgot_password),
                        color = AppGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(enabled = !isLoading) { onForgotPasswordClick() }
                            .padding(vertical = 4.dp)
                    )
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = AppGreen
                )
            }

            // --- BLOCO INFERIOR: Ações e Cadastro ---
            LoginFooter(
                onLoginClick = onLoginClick,
                onSignUpClick = onSignUpClick,
                enabled = !isLoading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    EvoFitTheme {
        LoginContent(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            isPasswordVisible = false,
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onForgotPasswordClick = {},
            onSignUpClick = {}
        )
    }
}
