package com.guaracode.evofit.presentation.ui.feature.authentication.screens

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.guaracode.evofit.presentation.ui.feature.authentication.components.*
import com.guaracode.evofit.presentation.ui.feature.authentication.state.LoginUiState
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel.LoginViewModel
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.theme.Dimens
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    authTracker: AuthTracker = koinInject(),
    onLoginSuccess: (Boolean) -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onAppleClick: () -> Unit = {},
    googleIcon: Painter? = null,
    appleIcon: Painter? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        authTracker.trackLoginScreenView()
    }

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
        onForgotPasswordClick = {
            authTracker.trackForgotPasswordClicked()
            onForgotPasswordClick()
        },
        onSignUpClick = onSignUpClick,
        onBackClick = onBackClick,
        onGoogleClick = onGoogleClick,
        onAppleClick = onAppleClick,
        googleIcon = googleIcon,
        appleIcon = appleIcon
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
    googleIcon: Painter? = null,
    appleIcon: Painter? = null,
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
            LoginHeader()

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

                LoginInputField(
                    label = "E-mail",
                    placeholder = "seu@email.com",
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading
                )

                LoginInputField(
                    label = "Senha",
                    placeholder = "Sua senha",
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.SpacingMediumSmall, bottom = Dimens.SpacingLarge),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Esqueceu sua senha?",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.clickable(enabled = !uiState.isLoading) { onForgotPasswordClick() }
                )
            }

            LoginButton(
                onClick = onLoginClick,
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading
            )

            if (googleIcon != null) {
                LoginSocialDivider()

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = Dimens.SpacingMedium),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
                ) {
                    SocialLoginButton(
                        text = "Google",
                        icon = googleIcon,
                        onClick = onGoogleClick,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isLoading
                    )
                    if (uiState.showAppleLogin && appleIcon != null) {
                        SocialLoginButton(
                            text = "Apple",
                            icon = appleIcon,
                            onClick = onAppleClick,
                            modifier = Modifier.weight(1f),
                            enabled = !uiState.isLoading
                        )
                    }
                }
            }
        }
    }
}
