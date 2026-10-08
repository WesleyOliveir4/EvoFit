package com.guaracode.evofit.presentation.ui.feature.splash.tracking

interface SplashTracker {
    fun trackSplashScreenView()
    fun trackSplashDestinationResolved(
        destination: String,
        isLoggedIn: Boolean,
        isOnboardingCompleted: Boolean
    )
}
