package com.example.evofit.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AppGreen,
    secondary = TextSecondary,
    tertiary = AppSurfaceVariant,
    background = AppDarkBg,
    surface = AppSurface,
    surfaceVariant = AppSurfaceVariant,
    outline = TextDisabled,
    outlineVariant = InputBorder,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = AppGreen,
    secondary = TextSecondary,
    tertiary = AppSurfaceVariant,
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFFEEEEEE),
    outline = Color.LightGray,
    outlineVariant = Color.Gray,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black
)

@Composable
fun EvoFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}