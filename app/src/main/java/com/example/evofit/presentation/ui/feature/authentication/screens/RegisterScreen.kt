package com.example.evofit.presentation.ui.feature.authentication.screens

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
import androidx.compose.material.icons.filled.Person
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.authentication.components.RegisterFooter
import com.example.evofit.presentation.ui.feature.authentication.components.RegisterHeader
import com.example.evofit.presentation.ui.feature.authentication.components.LoginInputField
import com.example.evofit.presentation.ui.feature.authentication.components.TermsCheckboxRow
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.RegisterUiState
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.RegisterViewModel
import com.example.evofit.presentation.ui.theme.*
import org.koin.androidx.compose.koinViewModel

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

    // NOTE (Passo 1 - somente UI): "nome", "confirmar senha" e "termos" ainda
    // não existem no RegisterViewModel/RegisterUiState atuais. Mantemos esses
    // campos como estado local por enquanto; a persistência do nome e as
    // regras de validação server-side serão movidas para o RegisterViewModel
    // no Passo 2 do plano (ver PLANO_FLUXO_AUTENTICACAO.md, seção 5).
    var name by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    val passwordsMismatch = confirmPassword.isNotEmpty() && confirmPassword != uiState.password
    val canSubmit = name.isNotBlank() &&
        uiState.email.isNotBlank() &&
        uiState.password.isNotBlank() &&
        !passwordsMismatch &&
        termsAccepted &&
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
        name = name,
        onNameChange = { name = it },
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        isConfirmPasswordVisible = isConfirmPasswordVisible,
        onToggleConfirmPasswordVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
        passwordsMismatch = passwordsMismatch,
        termsAccepted = termsAccepted,
        onTermsAcceptedChange = { termsAccepted = it },
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
    name: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    passwordsMismatch: Boolean,
    termsAccepted: Boolean,
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
            RegisterHeader()

            // --- BLOCO CENTRAL: Formulário ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo de Nome
                LoginInputField(
                    value = name,
                    onValueChange = onNameChange,
                    label = stringResource(id = R.string.register_label_name),
                    placeholder = stringResource(id = R.string.register_placeholder_name),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    },
                    enabled = !uiState.isLoading
                )

                // Campo de E-mail
                LoginInputField(
                    value = uiState.email,
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
                    enabled = !uiState.isLoading
                )

                // Campo de Senha
                LoginInputField(
                    value = uiState.password,
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
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = stringResource(id = R.string.register_label_confirm_password),
                    placeholder = stringResource(id = R.string.register_placeholder_confirm_password),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = TextSecondary
                        )
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
                    checked = termsAccepted,
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
            name = "",
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            confirmPassword = "",
            onConfirmPasswordChange = {},
            isConfirmPasswordVisible = false,
            onToggleConfirmPasswordVisibility = {},
            passwordsMismatch = false,
            termsAccepted = false,
            onTermsAcceptedChange = {},
            onTermsOfUseClick = {},
            onPrivacyPolicyClick = {},
            canSubmit = false,
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
