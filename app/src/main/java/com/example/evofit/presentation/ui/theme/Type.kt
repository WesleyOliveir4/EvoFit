package com.example.evofit.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Material 3 Typography configuration for EvoFit, using centralized dimensions from [Dimens].
 */
val Typography = Typography(
    // Large Titles (Welcome screens)
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontSize = Dimens.TextSizeDisplay,
        lineHeight = 38.sp
    ),

    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontSize = Dimens.TextSizeDisplay,
        lineHeight = 26.sp
    ),
    
    // Main Screen Titles
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = Dimens.TextSizeHeadlineLarge,
        lineHeight = 32.sp
    ),

    // Intermediate Titles
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontSize = Dimens.TextSizeHeadlineMedium,
        lineHeight = 30.sp
    ),

    // Dialog & Small Section Titles
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = Dimens.TextSizeHeadlineSmall
    ),

    // Logos & Highlights
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = Dimens.TextSizeExtraLarge
    ),

    // Standard Body Text (Inputs, Buttons)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = Dimens.TextSizeMedium,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Secondary Body Text (Subtitles)
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = Dimens.TextSizeMediumSmall,
        lineHeight = 21.sp
    ),

    // Small Text (Footers, Descriptions)
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = Dimens.TextSizeSmall
    ),

    // Tiny Text (Secondary Info)
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = Dimens.TextSizeExtraSmall
    )
)
