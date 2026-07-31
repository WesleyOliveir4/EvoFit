package com.example.evofit.presentation.ui.feature.authentication.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.example.evofit.presentation.ui.feature.authentication.components.NewPasswordFooter
import com.example.evofit.presentation.ui.feature.authentication.components.NewPasswordHeader
import com.example.evofit.presentation.ui.feature.authentication.state.NewPasswordUiState
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.NewPasswordViewModel
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

private const val MIN_PASSWORD_LENGTH = 6

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun NewPasswordScreen(
    oobCode: String,
    viewModel: NewPasswordViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onPasswordResetSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val passwordsMismatch = uiState.confirmPassword.isNotEmpty() && uiState.confirmPassword != uiState.password
    val isPasswordTooShort = uiState.password.isNotEmpty() && uiState.password.length < MIN_PASSWORD_LENGTH
    val canSubmit = uiState.password.length >= MIN_PASSWORD_LENGTH && !passwordsMismatch && !uiState.isLoading

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, context.getString(R.string.new_password_success), Toast.LENGTH_SHORT).show()
            onPasswordResetSuccess()
            viewModel.resetSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    NewPasswordContent(
        uiState = uiState,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onToggleConfirmPasswordVisibility = viewModel::onToggleConfirmPasswordVisibility,
        passwordsMismatch = passwordsMismatch,
        isPasswordTooShort = isPasswordTooShort,
        canSubmit = canSubmit,
        onBackClick = onBackClick,
        onSaveClick = { viewModel.onSaveClick(oobCode) }
    )
}

@Composable
fun NewPasswordContent(
    uiState: NewPasswordUiState,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    passwordsMismatch: Boolean,
    isPasswordTooShort: Boolean,
    canSubmit: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
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
                NewPasswordFooter(
                    onSaveClick = onSaveClick,
                    enabled = canSubmit,
                    isLoading = uiState.isLoading
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
            Column {
                NewPasswordHeader()

                Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
                    LoginInputField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        label = stringResource(id = R.string.new_password_label_password),
                        placeholder = stringResource(id = R.string.new_password_placeholder_password),
                        trailingIcon = {
                            IconButton(onClick = onTogglePasswordVisibility) {
                                Icon(
                                    imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (uiState.isPasswordVisible) {
                                        stringResource(id = R.string.login_content_desc_hide_password)
                                    } else {
                                        stringResource(id = R.string.login_content_desc_show_password)
                                    },
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        },
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        enabled = !uiState.isLoading
                    )

                    if (isPasswordTooShort) {
                        Text(
                            text = stringResource(id = R.string.new_password_error_too_short),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    LoginInputField(
                        value = uiState.confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        label = stringResource(id = R.string.new_password_label_confirm),
                        placeholder = stringResource(id = R.string.new_password_placeholder_confirm),
                        trailingIcon = {
                            IconButton(onClick = onToggleConfirmPasswordVisibility) {
                                Icon(
                                    imageVector = if (uiState.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (uiState.isConfirmPasswordVisible) {
                                        stringResource(id = R.string.login_content_desc_hide_password)
                                    } else {
                                        stringResource(id = R.string.login_content_desc_show_password)
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
                            text = stringResource(id = R.string.new_password_error_mismatch),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewPasswordScreenPreview() {
    EvoFitTheme {
        NewPasswordContent(
            uiState = NewPasswordUiState(),
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onConfirmPasswordChange = {},
            onToggleConfirmPasswordVisibility = {},
            passwordsMismatch = false,
            isPasswordTooShort = false,
            canSubmit = false,
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
