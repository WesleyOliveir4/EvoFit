package com.example.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.*

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.login_title),
            color = AppGreen,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
        Text(
            text = stringResource(id = R.string.login_subtitle),
            color = TextSecondary,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun LoginInputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = if (enabled) TextSecondary else TextSecondary.copy(alpha = 0.5f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = if (enabled) TextPrimary else TextSecondary, fontSize = 16.sp),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AppSurface,
                unfocusedContainerColor = AppSurface,
                focusedBorderColor = AppGreen,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = AppGreen,
                disabledContainerColor = AppSurface,
                disabledBorderColor = Color.Transparent,
                disabledTextColor = TextSecondary
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            placeholder = { Text(placeholder, color = TextSecondary.copy(alpha = 0.6f)) }
        )
    }
}

@Composable
fun LoginFooter(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    extraContent: @Composable (ColumnScope.() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppGreen,
                contentColor = Color.Black,
                disabledContainerColor = AppGreen.copy(alpha = 0.5f),
                disabledContentColor = Color.Black.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(28.dp),
            enabled = enabled
        ) {
            Text(
                text = stringResource(id = R.string.login_button_enter),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        extraContent?.invoke(this)

        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.login_no_account),
                color = TextSecondary,
                fontSize = 14.sp
            )
            Text(
                text = stringResource(id = R.string.login_sign_up),
                color = if (enabled) AppGreen else AppGreen.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = enabled) { onSignUpClick() }
            )
        }
    }
}

/**
 * "ou continue com" divider shown between the primary form and the social
 * login buttons (mock screen 2).
 */
@Composable
fun LoginSocialDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = InputBorder)
        Text(
            text = stringResource(id = R.string.login_or_continue_with),
            color = TextSecondary,
            fontSize = 12.sp
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = InputBorder)
    }
}

/**
 * Google and Apple social sign-in buttons. Both are backed by Firebase
 * Authentication (Google via Credential Manager + GoogleAuthProvider, Apple
 * via Firebase OAuthProvider) — wiring happens in the ViewModel layer.
 */
@Composable
fun SocialLoginButtons(
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onGoogleClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextPrimary
            ),
            border = BorderStroke(1.dp, InputBorder)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = stringResource(id = R.string.login_content_desc_google),
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.login_google),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedButton(
            onClick = onAppleClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextPrimary
            ),
            border = BorderStroke(1.dp, InputBorder)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_apple),
                contentDescription = stringResource(id = R.string.login_content_desc_apple),
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.login_apple),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun LoginHeaderPreview() {
    EvoFitTheme {
        LoginHeader()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun LoginInputFieldPreview() {
    EvoFitTheme {
        LoginInputField(
            value = "",
            onValueChange = {},
            label = "E-MAIL",
            placeholder = "Seu e-mail cadastrado",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextSecondary) }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun LoginFooterPreview() {
    EvoFitTheme {
        LoginFooter(onLoginClick = {}, onSignUpClick = {})
    }
}
