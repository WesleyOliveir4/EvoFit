package com.example.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * Logo + headline + subtitle block shown in the upper/central area of the
 * Pre-Login (welcome) screen.
 */
@Composable
fun PreLoginHeader(modifier: Modifier = Modifier) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_evofit),
            contentDescription = stringResource(id = R.string.pre_login_content_desc_logo),
            modifier = Modifier.size(Dimens.AuthLogoSizeOnboarding)
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingExtraExtraLarge))

        val fullTitle = stringResource(id = R.string.pre_login_title)
        val highlightPart = "melhor"
        val annotatedTitle = buildAnnotatedString {
            val startIndex = fullTitle.indexOf(highlightPart)
            if (startIndex >= 0) {
                append(fullTitle.substring(0, startIndex))
                withStyle(style = SpanStyle(color = primaryColor)) {
                    append(highlightPart)
                }
                append(fullTitle.substring(startIndex + highlightPart.length))
            } else {
                append(fullTitle)
            }
        }

        Text(
            text = annotatedTitle,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            text = stringResource(id = R.string.pre_login_subtitle),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Decorative page-indicator dots. The Pre-Login screen is the first step of
 * a potential onboarding carousel; only the first dot is active for now.
 */
@Composable
fun PreLoginPageIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int = 4,
    activeIndex: Int = 0
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isActive = index == activeIndex
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                    .size(if (isActive) Dimens.SpacingSmall else Dimens.SpacingTiny)
            )
        }
    }
}

/**
 * Primary CTA button ("Começar") that starts the authentication flow.
 */
@Composable
fun PreLoginFooter(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onStartClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightPrimary),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge)
    ) {
        Text(
            text = stringResource(id = R.string.pre_login_button_start),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun PreLoginHeaderPreview() {
    EvoFitTheme {
        PreLoginHeader()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun PreLoginFooterPreview() {
    EvoFitTheme {
        Column {
            PreLoginPageIndicator()
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            PreLoginFooter(onStartClick = {})
        }
    }
}
