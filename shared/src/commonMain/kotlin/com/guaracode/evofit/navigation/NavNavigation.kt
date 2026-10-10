package com.guaracode.evofit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.guaracode.evofit.presentation.ui.feature.authentication.screens.LoginScreen
import com.guaracode.evofit.presentation.ui.feature.authentication.screens.PreLoginScreen
import com.guaracode.evofit.presentation.ui.feature.authentication.screens.RecoverPasswordScreen
import com.guaracode.evofit.presentation.ui.feature.authentication.screens.RegisterScreen

sealed interface AuthScreen {
    object PreLogin : AuthScreen
    object Login : AuthScreen
    object Register : AuthScreen
    object RecoverPassword : AuthScreen
}

@Composable
fun AuthNavigation(
    onLoginSuccess: (Boolean) -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    var currentScreen by remember { mutableStateOf<AuthScreen>(AuthScreen.PreLogin) }

    when (currentScreen) {
        is AuthScreen.PreLogin -> {
            PreLoginScreen(
                onStartClick = {
                    currentScreen = AuthScreen.Login
                }
            )
        }
        is AuthScreen.Login -> {
            LoginScreen(
                onLoginSuccess = onLoginSuccess,
                onSignUpClick = {
                    currentScreen = AuthScreen.Register
                },
                onForgotPasswordClick = {
                    currentScreen = AuthScreen.RecoverPassword
                },
                onBackClick = {
                    currentScreen = AuthScreen.PreLogin
                }
            )
        }
        is AuthScreen.Register -> {
            RegisterScreen(
                onRegisterSuccess = onRegisterSuccess,
                onBackClick = {
                    currentScreen = AuthScreen.Login
                }
            )
        }
        is AuthScreen.RecoverPassword -> {
            RecoverPasswordScreen(
                onCodeSent = { _ ->
                    currentScreen = AuthScreen.Login
                },
                onBackClick = {
                    currentScreen = AuthScreen.Login
                }
            )
        }
    }
}
