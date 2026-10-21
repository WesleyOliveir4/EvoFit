package com.guaracode.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.forgot_password_button_continue
import evofit.shared.generated.resources.forgot_password_content_desc_illustration
import evofit.shared.generated.resources.forgot_password_subtitle
import evofit.shared.generated.resources.forgot_password_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ForgotPasswordIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(Dimens.AuthIllustrationSizeLarge),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.AuthIllustrationSizeMedium)
                .clip(RoundedCornerShape(Dimens.CornerRadiusExtraLarge))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MailOutline,
                contentDescription = stringResource(Res.string.forgot_password_content_desc_illustration),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.SpacingExtraLargePlus)
            )
        }
    }
}

@Composable
fun ForgotPasswordHeader(
    email: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
    ) {
        Text(
            text = stringResource(Res.string.forgot_password_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.forgot_password_subtitle, email),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ForgotPasswordFooter(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onContinueClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightPrimary),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(Dimens.CornerRadiusSmall)
    ) {
        Text(
            text = stringResource(Res.string.forgot_password_button_continue),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordPreview() {
    EvoFitTheme {
        Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
            ForgotPasswordIllustration()
            ForgotPasswordHeader(email = "test@evofit.com")
            ForgotPasswordFooter(onContinueClick = {})
        }
    }
}
