package com.guaracode.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.guaracode.evofit.presentation.ui.theme.Dimens

@Composable
fun PreLoginHeader(modifier: Modifier = Modifier) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        Spacer(modifier = Modifier.height(Dimens.SpacingExtraExtraLarge))

        val fullTitle = "Bem-vindo(a) ao\nseu novo melhor."
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
            text = "Treinos personalizados, evolução real e resultados de verdade.",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

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
            text = "Começar",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
