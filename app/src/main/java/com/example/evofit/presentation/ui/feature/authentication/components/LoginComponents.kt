package com.example.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Brand Logo and Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(IconContainerBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo_evofit),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "EvoFit",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Welcome Message
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Bem-vindo de volta",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Continue de onde parou na sua evolução.",
                color = TextSecondary,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun LoginInputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(color = TextPrimary, fontSize = 16.sp),
        label = { Text(label) },
        placeholder = { Text(placeholder, color = TextSecondary.copy(alpha = 0.5f)) },
        trailingIcon = trailingIcon,
        enabled = enabled,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppSurface,
            unfocusedContainerColor = AppSurface,
            focusedBorderColor = AppGreen,
            unfocusedBorderColor = AppSurfaceVariant,
            cursorColor = AppGreen,
            disabledContainerColor = AppSurface,
            disabledBorderColor = AppSurfaceVariant,
            disabledTextColor = TextSecondary,
            focusedLabelColor = AppGreen,
            unfocusedLabelColor = TextSecondary,
            focusedPlaceholderColor = TextSecondary.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = TextSecondary.copy(alpha = 0.5f)
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppGreen,
            contentColor = Color.Black,
            disabledContainerColor = AppGreen.copy(alpha = 0.5f),
            disabledContentColor = Color.Black.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.Black,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = stringResource(id = R.string.login_button_enter),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SocialLoginButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, AppSurfaceVariant, RoundedCornerShape(20.dp))
            .background(Color.Transparent)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LoginSocialDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = AppSurfaceVariant)
        Text(
            text = stringResource(id = R.string.login_or_continue_with),
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = AppSurfaceVariant)
    }
}

@Composable
fun LoginRegistrationFooter(
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier.padding(top = 40.dp, bottom = 24.dp),
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
            color = AppGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(enabled = enabled) { onSignUpClick() }
        )
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
            placeholder = "seu@email.com"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun LoginButtonPreview() {
    EvoFitTheme {
        LoginButton(onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun SocialLoginButtonPreview() {
    EvoFitTheme {
        SocialLoginButton(
            text = "Google",
            icon = painterResource(id = R.drawable.ic_google),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun LoginRegistrationFooterPreview() {
    EvoFitTheme {
        LoginRegistrationFooter(onSignUpClick = {})
    }
}
