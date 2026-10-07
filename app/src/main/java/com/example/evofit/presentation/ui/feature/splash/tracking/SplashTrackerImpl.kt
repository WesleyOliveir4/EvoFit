package com.example.evofit.presentation.ui.feature.splash.tracking

import com.example.evofit.core.monitoring.CrashReporter

class SplashTrackerImpl(
    private val crashReporter: CrashReporter
) : SplashTracker {

    override fun trackSplashScreenView() {
        crashReporter.logEvent("screen_view: splash")
        crashReporter.setCustomKey("current_screen", "splash")
    }

    override fun trackSplashDestinationResolved(
        destination: String,
        isLoggedIn: Boolean,
        isOnboardingCompleted: Boolean
    ) {
        crashReporter.logEvent("splash_destination_resolved: $destination (logged_in=$isLoggedIn, onboard_completed=$isOnboardingCompleted)")
    }
}
