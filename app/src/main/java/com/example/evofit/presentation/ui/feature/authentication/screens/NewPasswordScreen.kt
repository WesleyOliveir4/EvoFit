package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.AuthBackButton
import com.example.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.example.evofit.presentation.ui.feature.authentication.components.NewPasswordFooter
import com.example.evofit.presentation.ui.feature.authentication.components.NewPasswordHeader
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextSecondary

private const val MIN_PASSWORD_LENGTH = 6

/**
 * "Criar nova senha" screen (mock screen 7 — not present in the reference
 * images, designed following the same visual language as the other screens
 * of the flow). Confirms the [oobCode]/code validated on [VerifyCodeScreen]
 * and lets the user define a new password.
 */
@Composable
fun NewPasswordScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (newPassword: String) -> Unit = {}
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val passwordsMismatch = confirmPassword.isNotEmpty() && confirmPassword != password
    val isPasswordTooShort = password.isNotEmpty() && password.length < MIN_PASSWORD_LENGTH
    val canSubmit = password.length >= MIN_PASSWORD_LENGTH && !passwordsMismatch

    NewPasswordContent(
        password = password,
        onPasswordChange = { password = it },
        isPasswordVisible = isPasswordVisible,
        onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        isConfirmPasswordVisible = isConfirmPasswordVisible,
        onToggleConfirmPasswordVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
        passwordsMismatch = passwordsMismatch,
        isPasswordTooShort = isPasswordTooShort,
        canSubmit = canSubmit,
        onBackClick = onBackClick,
        onSaveClick = { onSaveClick(password) }
    )
}

@Composable
fun NewPasswordContent(
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    passwordsMismatch: Boolean,
    isPasswordTooShort: Boolean,
    canSubmit: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
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
                    contentDescription = stringResource(id = R.string.new_password_back_desc),
                    modifier = Modifier.padding(top = 16.dp)
                )

                NewPasswordHeader()

                Spacer(modifier = Modifier.height(32.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    LoginInputField(
                        value = password,
                        onValueChange = onPasswordChange,
                        label = stringResource(id = R.string.new_password_label_password),
                        placeholder = stringResource(id = R.string.new_password_placeholder_password),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary)
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

                    if (isPasswordTooShort) {
                        Text(
                            text = stringResource(id = R.string.new_password_error_too_short),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }

                    LoginInputField(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        label = stringResource(id = R.string.new_password_label_confirm),
                        placeholder = stringResource(id = R.string.new_password_placeholder_confirm),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary)
                        },
                        trailingIcon = {
                            IconButton(onClick = onToggleConfirmPasswordVisibility) {
                                Icon(
                                    imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (isConfirmPasswordVisible) {
                                        stringResource(id = R.string.login_content_desc_hide_password)
                                    } else {
                                        stringResource(id = R.string.login_content_desc_show_password)
                                    },
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        enabled = !isLoading
                    )

                    if (passwordsMismatch) {
                        Text(
                            text = stringResource(id = R.string.new_password_error_mismatch),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            NewPasswordFooter(
                onSaveClick = onSaveClick,
                enabled = canSubmit,
                isLoading = isLoading,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewPasswordScreenPreview() {
    EvoFitTheme {
        NewPasswordContent(
            password = "",
            onPasswordChange = {},
            isPasswordVisible = false,
            onTogglePasswordVisibility = {},
            confirmPassword = "",
            onConfirmPasswordChange = {},
            isConfirmPasswordVisible = false,
            onToggleConfirmPasswordVisibility = {},
            passwordsMismatch = false,
            isPasswordTooShort = false,
            canSubmit = false,
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
