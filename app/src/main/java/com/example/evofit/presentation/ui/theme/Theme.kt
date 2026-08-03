package com.example.evofit.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class EvoCustomColors(
    val orange: Color,
    val blue: Color,
    val green: Color,
    val purple: Color,
    val red: Color,
    val yellow: Color
)

val LocalEvoCustomColors = staticCompositionLocalOf {
    EvoCustomColors(
        orange = Color.Unspecified,
        blue = Color.Unspecified,
        green = Color.Unspecified,
        purple = Color.Unspecified,
        red = Color.Unspecified,
        yellow = Color.Unspecified,
    )
}

val MaterialTheme.evoColors: EvoCustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalEvoCustomColors.current

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
        else -> DarkColorScheme
    }

    val evoCustomColors = EvoCustomColors(
        orange = EvoOrange,
        blue = EvoBlue,
        green = EvoGreen,
        purple = EvoPurple,
        red = EvoRed,
        yellow = EvoYellow,
    )

    CompositionLocalProvider(
        LocalEvoCustomColors provides evoCustomColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}