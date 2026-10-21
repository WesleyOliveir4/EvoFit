package com.guaracode.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.recover_password_button_send
import evofit.shared.generated.resources.recover_password_subtitle
import evofit.shared.generated.resources.recover_password_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RecoverPasswordHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Dimens.SpacingLarge),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = stringResource(Res.string.recover_password_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(Res.string.recover_password_subtitle),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun RecoverPasswordFooter(
    onSendCodeClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onSendCodeClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightPrimary),
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
                text = stringResource(Res.string.recover_password_button_send),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
private fun RecoverPasswordPreview() {
    EvoFitTheme {
        Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
            RecoverPasswordHeader()
            RecoverPasswordFooter(onSendCodeClick = {})
        }
    }
}
