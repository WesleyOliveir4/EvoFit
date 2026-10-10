package com.guaracode.evofit.presentation.ui.feature.authentication.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import com.guaracode.evofit.presentation.ui.theme.Dimens

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Dimens.SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.SpacingExtraExtraLarge)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(Dimens.CornerRadiusSmall)),
                contentAlignment = Alignment.Center
            ) {
                // Logo placeholder or icon
            }
            Text(
                text = "EvoFit",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            Text(
                text = "Bem-vindo de volta",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Continue de onde parou na sua evolução.",
                color = MaterialTheme.colorScheme.secondary,
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
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        label = { Text(label) },
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)) },
        trailingIcon = trailingIcon,
        enabled = enabled,
        singleLine = true,
        shape = RoundedCornerShape(Dimens.CornerRadiusExtraSmall),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledTextColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
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
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dimens.SpacingLarge),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = Dimens.SpacingExtraExtraSmall
            )
        } else {
            Text(
                text = "Entrar",
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
            .border(Dimens.BorderWidthThin, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(Dimens.CornerRadiusCardSmall))
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
                color = MaterialTheme.colorScheme.onSurface,
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
        HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.surfaceVariant)
        Text(
            text = "ou continue com",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = Dimens.SpacingMedium)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.surfaceVariant)
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
            text = "Não possui uma conta?",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Cadastre-se",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.clickable(enabled = enabled) { onSignUpClick() }
        )
    }
}
