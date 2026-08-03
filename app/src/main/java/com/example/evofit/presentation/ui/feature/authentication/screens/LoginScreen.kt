package com.example.evofit.presentation.ui.feature.authentication.screens

import android.app.Activity
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.apple.AppleSignInHandler
import com.example.evofit.presentation.ui.feature.authentication.components.*
import com.example.evofit.presentation.ui.feature.authentication.google.GoogleSignInHandler
import com.example.evofit.presentation.ui.feature.authentication.state.LoginUiState
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.LoginViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    googleSignInHandler: GoogleSignInHandler = koinInject(),
    appleSignInHandler: AppleSignInHandler = koinInject(),
    onLoginSuccess: (Boolean) -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess(uiState.isOnboardingCompleted)
            viewModel.resetSuccess()
        }
    }

    LoginContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onLoginClick = viewModel::onLoginClick,
        onForgotPasswordClick = onForgotPasswordClick,
        onSignUpClick = onSignUpClick,
        onBackClick = onBackClick,
        onGoogleClick = {
            scope.launch {
                googleSignInHandler.signIn().onSuccess { idToken ->
                    viewModel.onGoogleLoginClick(idToken)
                }.onFailure { error ->
                    // Erro tratado no ViewModel se necessário
                }
            }
        },
        onAppleClick = {
            val activity = context as? Activity
            if (activity != null) {
                scope.launch {
                    appleSignInHandler.signIn(activity).onSuccess {
                        viewModel.onAppleLoginClick()
                    }.onFailure { error ->
                        // Erro tratado no ViewModel se necessário
                    }
                }
            }
        }
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit,
    onGoogleClick: () -> Unit = {},
    onAppleClick: () -> Unit = {},
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
                    .padding(bottom = Dimens.SpacingMedium),
                contentAlignment = Alignment.Center
            ) {
                LoginRegistrationFooter(
                    onSignUpClick = onSignUpClick,
                    enabled = !uiState.isLoading
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // --- HEADER: Logo e Mensagem de Boas-Vindas ---
            LoginHeader()

            // --- INPUTS: Formulário de Credenciais ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.SectionSpacing),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
            ) {
                if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = Dimens.SpacingSmall)
                    )
                }

                // E-mail
                LoginInputField(
                    label = stringResource(id = R.string.login_label_email),
                    placeholder = stringResource(id = R.string.login_placeholder_email),
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading
                )

                // Senha
                LoginInputField(
                    label = stringResource(id = R.string.login_label_password),
                    placeholder = stringResource(id = R.string.login_placeholder_password),
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                            )
                        }
                    },
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading
                )
            }

            // Esqueci minha senha
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.SpacingMediumSmall, bottom = Dimens.SpacingLarge),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = stringResource(id = R.string.login_forgot_password),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.clickable(enabled = !uiState.isLoading) { onForgotPasswordClick() }
                )
            }

            // Botão Entrar
            LoginButton(
                onClick = onLoginClick,
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading
            )

            // --- DIVISOR: Ou continue com ---
            LoginSocialDivider()

            // --- BOTÕES SOCIAIS: Google & Apple ---
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = Dimens.SpacingMedium),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                SocialLoginButton(
                    text = "Google",
                    icon = painterResource(id = R.drawable.ic_google),
                    onClick = onGoogleClick,
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading
                )
                if (uiState.showAppleLogin) {
                    SocialLoginButton(
                        text = "Apple",
                        icon = painterResource(id = R.drawable.ic_apple),
                        onClick = onAppleClick,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isLoading
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun LoginScreenPreview() {
    EvoFitTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onForgotPasswordClick = {},
            onSignUpClick = {},
            onBackClick = {}
        )
    }
}
