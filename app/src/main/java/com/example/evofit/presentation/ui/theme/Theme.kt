package com.example.evofit.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AppGreen,
    onPrimary = Color.Black,
    primaryContainer = IconContainerBg,
    onPrimaryContainer = AppGreen,
    
    secondary = TextSecondary,
    onSecondary = Color.White,
    secondaryContainer = WelcomeBoxBg,
    onSecondaryContainer = AppGreen,
    
    tertiary = EvoWarningYellow,
    onTertiary = Color.Black,
    tertiaryContainer = EvoIconBgYellow,
    onTertiaryContainer = EvoWarningYellow,
    
    background = AppDarkBg,
    onBackground = TextPrimary,
    
    surface = AppSurface,
    onSurface = TextPrimary,
    
    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = TextTertiary,
    
    error = EvoDestructiveRed,
    onError = Color.Black,
    errorContainer = EvoIconBgRed,
    onErrorContainer = EvoDestructiveRed,
    
    outline = TextDisabled,
    outlineVariant = InputBorder
)

private val LightColorScheme = lightColorScheme(
    primary = AppGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5E9),
    onPrimaryContainer = Color(0xFF2E7D32),
    
    secondary = Color(0xFF757575),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFF1F8E9),
    onSecondaryContainer = Color(0xFF33691E),
    
    tertiary = Color(0xFFFBC02D),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFF9C4),
    onTertiaryContainer = Color(0xFFF57F17),
    
    background = Color.White,
    onBackground = Color.Black,
    
    surface = Color(0xFFF5F5F5),
    onSurface = Color.Black,
    
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color.DarkGray,
    
    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    
    outline = Color.LightGray,
    outlineVariant = Color.Gray
)

@Composable
fun EvoFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> DarkColorScheme // Keeping Dark as default for now as requested or common in sports apps
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}