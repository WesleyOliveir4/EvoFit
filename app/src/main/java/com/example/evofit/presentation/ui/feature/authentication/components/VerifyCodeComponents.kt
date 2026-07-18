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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.*

const val VERIFY_CODE_LENGTH = 6

@Composable
fun VerifyCodeHeader(
    email: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.verify_code_title),
            color = TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = stringResource(id = R.string.verify_code_subtitle_format, email),
            color = TextSecondary,
            fontSize = 16.sp,
            lineHeight = 22.sp
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
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppSurface)
            .border(
                width = if (isActive) 2.dp else 0.dp,
                color = if (isActive) AppGreen else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
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
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppGreen,
            contentColor = Color.Black,
            disabledContainerColor = AppGreen.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.Black,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = stringResource(id = R.string.verify_code_button_verify),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
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
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.verify_code_not_received),
            color = TextSecondary,
            fontSize = 14.sp,
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
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = stringResource(id = R.string.verify_code_resend),
                color = AppGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
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
