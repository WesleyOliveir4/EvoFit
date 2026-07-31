package com.example.evofit.presentation.ui.feature.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

const val VERIFY_CODE_LENGTH = 6

@Composable
fun VerifyCodeHeader(
    email: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Dimens.SpacingLarge),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = stringResource(id = R.string.verify_code_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(id = R.string.verify_code_subtitle_format, email),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Six single-digit boxes used to input the verification code, matching mock
 * screen 6. [code] holds the digits typed so far (0..[VERIFY_CODE_LENGTH]).
 */
@Composable
fun OtpCodeInput(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = VERIFY_CODE_LENGTH,
    enabled: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }

    Box(modifier = modifier) {
        // Hidden field that actually captures keyboard input; the visible
        // boxes below are purely decorative and reflect its value.
        BasicTextField(
            value = code,
            onValueChange = { newValue ->
                val digitsOnly = newValue.filter { it.isDigit() }.take(length)
                onCodeChange(digitsOnly)
            },
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
            textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
            modifier = Modifier
                .matchParentSize()
                .focusRequester(focusRequester)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { focusRequester.requestFocus() },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(length) { index ->
                val digit = code.getOrNull(index)?.toString() ?: ""
                val isActive = index == code.length
                OtpDigitBox(digit = digit, isActive = isActive)
            }
        }
    }
}

@Composable
private fun OtpDigitBox(
    digit: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(Dimens.MinimumTouchTarget)
            .clip(RoundedCornerShape(Dimens.CornerRadiusMedium))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = if (isActive) Dimens.SpacingExtraExtraSmall else Dimens.SpacingNone,
                color = if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun VerifyCodeFooter(
    onVerifyClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onVerifyClick,
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
                text = stringResource(id = R.string.verify_code_button_verify),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

/**
 * "Não recebeu o código? Reenviar código (00:45)" row. While [secondsRemaining]
 * is greater than zero the resend action is shown disabled with a countdown;
 * once it reaches zero the user can tap to resend.
 */
@Composable
fun VerifyCodeResendRow(
    secondsRemaining: Int,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall)
    ) {
        Text(
            text = stringResource(id = R.string.verify_code_not_received),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        if (secondsRemaining > 0) {
            val minutes = secondsRemaining / 60
            val seconds = secondsRemaining % 60
            Text(
                text = stringResource(
                    id = R.string.verify_code_resend_format,
                    "%02d:%02d".format(minutes, seconds)
                ),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        } else {
            Text(
                text = stringResource(id = R.string.verify_code_resend),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { onResendClick() }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun OtpCodeInputPreview() {
    EvoFitTheme {
        OtpCodeInput(code = "12", onCodeChange = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun VerifyCodeResendRowPreview() {
    EvoFitTheme {
        VerifyCodeResendRow(secondsRemaining = 45, onResendClick = {})
    }
}
