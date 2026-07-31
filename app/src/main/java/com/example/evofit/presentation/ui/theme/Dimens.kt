package com.example.evofit.presentation.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized dimensions for the EvoFit project, following Material 3 guidelines.
 */
object Dimens {
    // Generic Spacing
    val SpacingNone = 0.dp
    val SpacingExtraExtraSmall = 2.dp // XXS
    val SpacingExtraSmall = 4.dp      // XS
    val SpacingTiny = 6.dp            // Tiny
    val SpacingSmall = 8.dp           // S
    val SpacingMediumSmall = 12.dp    // MS
    val SpacingMedium = 16.dp          // M
    val SpacingLarge = 24.dp           // L
    val SpacingExtraLarge = 24.dp      // XL - Adjusted from 32dp to fit 3-button navigation
    val SpacingExtraExtraLarge = 40.dp // XXL
    val SpacingExtraLargePlus = 40.dp  // XL+ - Adjusted from 64dp to fit 3-button navigation

    // Layout Margins & Paddings
    val ScreenPaddingHorizontal = 16.dp
    val ScreenPaddingVertical = 16.dp
    val SectionSpacing = 24.dp         // Adjusted from 32dp to fit 3-button navigation

    /**
     * Layout Recommendations for Authentication Screens:
     * 1. Use Scaffold with systemBarsPadding().
     * 2. Prefer SectionSpacing (24.dp) between major blocks to avoid scroll on 3-button navigation devices.
     * 3. Use SpacingExtraLargePlus (40.dp) for top headers instead of legacy 64dp.
     * 4. Ensure horizontal padding is ScreenPaddingHorizontal (16.dp).
     */

    // Component Heights
    val ButtonHeightPrimary = 56.dp
    val ButtonHeightSecondary = 48.dp
    val TextFieldHeight = 56.dp
    val SearchBarHeight = 56.dp
    val TopAppBarHeightSmall = 64.dp
    val TopAppBarHeightMedium = 112.dp
    val TopAppBarHeightLarge = 152.dp
    val BottomNavigationHeight = 80.dp

    // Floating Action Buttons (FAB)
    val FabSizeDefault = 56.dp
    val FabSizeSmall = 40.dp
    val FabSizeLarge = 96.dp

    // Icons & Media
    val IconSizeSmall = 20.dp
    val IconSizeMedium = 18.dp
    val IconSizeDefault = 24.dp
    val IconSizeLarge = 32.dp
    val AvatarSizeDefault = 56.dp

    // Authentication Flow Specifics
    val AuthLogoSizeOnboarding = 156.dp
    val AuthIllustrationSizeLarge = 160.dp
    val AuthIllustrationSizeMedium = 140.dp
    val AuthBadgeSizeDefault = 36.dp

    // Corners & Radii
    val CornerRadiusExtraSmall = 8.dp
    val CornerRadiusSmall = 12.dp
    val CornerRadiusMedium = 14.dp
    val CornerRadiusDefault = 16.dp
    val CornerRadiusCard = 24.dp // Standard range 20-24dp
    val CornerRadiusCardSmall = 20.dp
    val CornerRadiusLarge = 28.dp
    val CornerRadiusExtraLarge = 32.dp

    // Border & Elevation
    val BorderWidthThin = 1.dp
    val ElevationNone = 0.dp
    val ElevationLow = 2.dp

    // Dropdown & Specifics
    val DropdownMenuWidth = 120.dp

    // Previews & Temporary
    val PreviewHeightLarge = 300.dp

    // Accessibility
    val MinimumTouchTarget = 48.dp

    // Text Sizes (SP)
    val TextSizeTiny = 12.sp
    val TextSizeExtraSmall = 13.sp
    val TextSizeSmall = 14.sp
    val TextSizeMediumSmall = 15.sp
    val TextSizeMedium = 16.sp
    val TextSizeLarge = 20.sp
    val TextSizeHeadlineSmall = 22.sp
    val TextSizeHeadlineMedium = 26.sp
    val TextSizeHeadlineLarge = 28.sp
    val TextSizeDisplay = 32.sp
    val TextSizeExtraLarge = 24.sp
}
