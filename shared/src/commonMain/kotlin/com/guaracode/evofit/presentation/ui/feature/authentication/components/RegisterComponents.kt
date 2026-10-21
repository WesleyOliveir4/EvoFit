package com.guaracode.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TermsCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsOfUseClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
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
        FlowRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall)
        ) {
            Text(
                text = stringResource(Res.string.register_terms_agree_prefix),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = stringResource(Res.string.register_terms_of_use),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable(enabled = enabled) { onTermsOfUseClick() }
            )
            Text(
                text = stringResource(Res.string.register_terms_and),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = stringResource(Res.string.register_privacy_policy),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable(enabled = enabled) { onPrivacyPolicyClick() }
            )
        }
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
            .padding(bottom = Dimens.SpacingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
    ) {
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeightPrimary),
            enabled = enabled && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(Dimens.CornerRadiusSmall)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dimens.IconSizeDefault),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = Dimens.SpacingExtraExtraSmall
                )
            } else {
                Text(
                    text = stringResource(Res.string.register_button_confirm),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Row(
            modifier = Modifier.padding(bottom = Dimens.SpacingSmall),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.register_already_have_account),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(Res.string.register_login),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}

@Preview
@Composable
private fun RegisterPreview() {
    EvoFitTheme {
        Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
            RegisterHeader()
            TermsCheckboxRow(checked = false, onCheckedChange = {}, onTermsOfUseClick = {}, onPrivacyPolicyClick = {})
            RegisterFooter(isLoading = false, onRegisterClick = {}, onLoginClick = {})
        }
    }
}
