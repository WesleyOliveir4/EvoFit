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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.*

/**
 * Logo + headline + subtitle block shown in the upper/central area of the
 * Pre-Login (welcome) screen.
 */
@Composable
fun PreLoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_evofit),
            contentDescription = stringResource(id = R.string.pre_login_content_desc_logo),
            modifier = Modifier.size(56.dp)
        )

        Text(
            text = stringResource(id = R.string.pre_login_title),
            color = TextPrimary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 38.sp
        )

        Text(
            text = stringResource(id = R.string.pre_login_subtitle),
            color = TextSecondary,
            fontSize = 16.sp,
            lineHeight = 22.sp
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
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isActive = index == activeIndex
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isActive) AppGreen else TextSecondary.copy(alpha = 0.3f))
                    .size(if (isActive) 8.dp else 6.dp)
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
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppGreen,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        Text(
            text = stringResource(id = R.string.pre_login_button_start),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
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
            Spacer(modifier = Modifier.height(16.dp))
            PreLoginFooter(onStartClick = {})
        }
    }
}
