package com.example.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * Envelope illustration with a small "edit" badge, matching the artwork on
 * mock screen 4 ("Esqueci minha senha").
 */
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
                contentDescription = stringResource(id = R.string.forgot_password_content_desc_illustration),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.SpacingExtraLargePlus)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(Dimens.AuthBadgeSizeDefault),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(Dimens.IconSizeMedium)
            )
        }
    }
}

@Composable
fun ForgotPasswordHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
    ) {
        Text(
            text = stringResource(id = R.string.forgot_password_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.forgot_password_subtitle),
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
            text = stringResource(id = R.string.forgot_password_button_continue),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ForgotPasswordIllustrationPreview() {
    EvoFitTheme {
        ForgotPasswordIllustration()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ForgotPasswordHeaderPreview() {
    EvoFitTheme {
        ForgotPasswordHeader()
    }
}
