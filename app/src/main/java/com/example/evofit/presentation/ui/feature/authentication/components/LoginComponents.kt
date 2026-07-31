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
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.*

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Dimens.SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
    ) {
        // Brand Logo and Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.SpacingExtraExtraLarge)
                    .background(IconContainerBg, RoundedCornerShape(Dimens.CornerRadiusSmall)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo_evofit),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(Dimens.IconSizeDefault)
                )
            }
            Text(
                text = "EvoFit",
                color = TextPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Welcome Message
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            Text(
                text = "Bem-vindo de volta",
                color = TextPrimary,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Continue de onde parou na sua evolução.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
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
        modifier = modifier.fillMaxWidth().heightIn(min = Dimens.TextFieldHeight),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextPrimary),
        label = { Text(label) },
        placeholder = { Text(placeholder, color = TextSecondary.copy(alpha = 0.5f)) },
        trailingIcon = trailingIcon,
        enabled = enabled,
        singleLine = true,
        shape = RoundedCornerShape(Dimens.CornerRadiusExtraSmall),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppSurface,
            unfocusedContainerColor = AppSurface,
            focusedBorderColor = TextPrimary,
            unfocusedBorderColor = AppSurfaceVariant,
            cursorColor = TextPrimary,
            disabledContainerColor = AppSurface,
            disabledBorderColor = AppSurfaceVariant,
            disabledTextColor = TextSecondary,
            focusedLabelColor = TextPrimary,
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
            .height(Dimens.ButtonHeightPrimary),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppGreen,
            contentColor = Color.Black,
            disabledContainerColor = AppGreen.copy(alpha = 0.5f),
            disabledContentColor = Color.Black.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dimens.SpacingLarge),
                color = Color.Black,
                strokeWidth = Dimens.SpacingExtraExtraSmall
            )
        } else {
            Text(
                text = stringResource(id = R.string.login_button_enter),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
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
            .height(Dimens.ButtonHeightSecondary)
            .clip(RoundedCornerShape(Dimens.CornerRadiusSmall))
            .border(Dimens.BorderWidthThin, AppSurfaceVariant, RoundedCornerShape(Dimens.CornerRadiusCardSmall))
            .background(Color.Transparent)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
            Text(
                text = text,
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun LoginSocialDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SectionSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = AppSurfaceVariant)
        Text(
            text = stringResource(id = R.string.login_or_continue_with),
            color = TextSecondary,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = Dimens.SpacingMedium)
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
        modifier = modifier.padding(top = Dimens.SpacingExtraExtraLarge, bottom = Dimens.SpacingLarge),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.login_no_account),
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = stringResource(id = R.string.login_sign_up),
            color = AppGreen,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
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
