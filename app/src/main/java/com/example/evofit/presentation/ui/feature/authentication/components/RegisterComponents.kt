package com.example.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.*

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
            text = stringResource(id = R.string.register_title),
            color = TextPrimary,
            style = MaterialTheme.typography.displayLarge,
            letterSpacing = 0.5.sp
        )
        Text(
            text = stringResource(id = R.string.register_subtitle),
            color = TextSecondary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * "Eu concordo com os Termos de Uso e a Política de Privacidade" checkbox row
 * shown at the bottom of the registration form (mock screen 3).
 */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
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
                checkedColor = AppGreen,
                uncheckedColor = TextSecondary,
                checkmarkColor = Color.Black
            )
        )
        FlowRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall)
        ) {
            Text(
                text = stringResource(id = R.string.register_terms_agree_prefix),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = stringResource(id = R.string.register_terms_of_use),
                color = AppGreen,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable(enabled = enabled) { onTermsOfUseClick() }
            )
            Text(
                text = stringResource(id = R.string.register_terms_and),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = stringResource(id = R.string.register_privacy_policy),
                color = AppGreen,
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
                containerColor = AppGreen,
                contentColor = Color.Black,
                disabledContainerColor = AppGreen.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(Dimens.CornerRadiusSmall)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dimens.IconSizeDefault),
                    color = Color.Black,
                    strokeWidth = Dimens.SpacingExtraExtraSmall
                )
            } else {
                Text(
                    text = stringResource(id = R.string.register_button_confirm),
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
                text = stringResource(id = R.string.register_already_have_account),
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(id = R.string.register_login),
                color = AppGreen,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun TermsCheckboxRowPreview() {
    EvoFitTheme {
        TermsCheckboxRow(
            checked = false,
            onCheckedChange = {},
            onTermsOfUseClick = {},
            onPrivacyPolicyClick = {}
        )
    }
}
