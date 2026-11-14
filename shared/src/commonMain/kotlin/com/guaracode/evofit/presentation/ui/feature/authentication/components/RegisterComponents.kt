package com.guaracode.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.register_already_have_account
import evofit.shared.generated.resources.register_button_confirm
import evofit.shared.generated.resources.register_login
import evofit.shared.generated.resources.register_privacy_policy
import evofit.shared.generated.resources.register_subtitle
import evofit.shared.generated.resources.register_terms_and
import evofit.shared.generated.resources.register_terms_agree_prefix
import evofit.shared.generated.resources.register_terms_of_use
import evofit.shared.generated.resources.register_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RegisterHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Dimens.SpacingExtraLargePlus),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = stringResource(Res.string.register_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.displayLarge,
            letterSpacing = 0.5.sp
        )
        Text(
            text = stringResource(Res.string.register_subtitle),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TermsCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsOfUseClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val prefix = stringResource(Res.string.register_terms_agree_prefix)
    val terms = stringResource(Res.string.register_terms_of_use)
    val andText = stringResource(Res.string.register_terms_and)
    val privacy = stringResource(Res.string.register_privacy_policy)

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val annotatedString = remember(prefix, terms, andText, privacy, primaryColor, secondaryColor) {
        buildAnnotatedString {
            withStyle(SpanStyle(color = secondaryColor)) {
                append("$prefix ")
            }
            pushStringAnnotation(tag = "TERMS", annotation = "terms")
            withStyle(SpanStyle(color = primaryColor, fontWeight = FontWeight.Bold)) {
                append(terms)
            }
            pop()
            withStyle(SpanStyle(color = secondaryColor)) {
                append(" $andText ")
            }
            pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
            withStyle(SpanStyle(color = primaryColor, fontWeight = FontWeight.Bold)) {
                append(privacy)
            }
            pop()
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.secondary,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(1f),
            onClick = { offset ->
                if (!enabled) return@ClickableText
                annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                    .firstOrNull()?.let { onTermsOfUseClick() }
                annotatedString.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                    .firstOrNull()?.let { onPrivacyPolicyClick() }
            }
        )
    }
}

@Composable
fun RegisterFooter(
    isLoading: Boolean,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.SpacingMediumSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
    ) {
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeightPrimary),
            enabled = enabled && !isLoading,
            shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dimens.SpacingLarge),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = Dimens.BorderWidthThin
                )
            } else {
                Text(
                    text = stringResource(Res.string.register_button_confirm),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = Dimens.SpacingExtraSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall)
        ) {
            Text(
                text = stringResource(Res.string.register_already_have_account),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(Res.string.register_login),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}

@Preview
@Composable
private fun TermsCheckboxRowPreview() {
    EvoFitTheme {
        TermsCheckboxRow(
            checked = true,
            onCheckedChange = {},
            onTermsOfUseClick = {},
            onPrivacyPolicyClick = {}
        )
    }
}
