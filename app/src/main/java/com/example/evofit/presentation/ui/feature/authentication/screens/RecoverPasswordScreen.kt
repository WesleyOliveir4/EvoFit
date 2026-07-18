package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.AuthBackButton
import com.example.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.example.evofit.presentation.ui.feature.authentication.components.RecoverPasswordFooter
import com.example.evofit.presentation.ui.feature.authentication.components.RecoverPasswordHeader
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextSecondary

/**
 * "Recuperar senha" screen (mock screen 5). Collects the e-mail that will
 * receive the verification code. Business logic (SendPasswordResetCodeUseCase
 * + Firebase) will be wired to this screen's ViewModel in a later step of the
 * plan — for now the e-mail is kept as local UI state.
 */
@Composable
fun RecoverPasswordScreen(
    onBackClick: () -> Unit = {},
    onSendCodeClick: (email: String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    RecoverPasswordContent(
        email = email,
        onEmailChange = { email = it },
        canSubmit = isEmailValid,
        onBackClick = onBackClick,
        onSendCodeClick = { onSendCodeClick(email) }
    )
}

@Composable
fun RecoverPasswordContent(
    email: String,
    onEmailChange: (String) -> Unit,
    canSubmit: Boolean,
    onBackClick: () -> Unit,
    onSendCodeClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppDarkBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                AuthBackButton(
                    onBackClick = onBackClick,
                    contentDescription = stringResource(id = R.string.recover_password_back_desc),
                    modifier = Modifier.padding(top = 16.dp)
                )

                RecoverPasswordHeader()

                Spacer(modifier = Modifier.height(32.dp))

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
            }

            RecoverPasswordFooter(
                onSendCodeClick = onSendCodeClick,
                enabled = canSubmit,
                isLoading = isLoading,
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
            email = "",
            onEmailChange = {},
            canSubmit = false,
            onBackClick = {},
            onSendCodeClick = {}
        )
    }
}
