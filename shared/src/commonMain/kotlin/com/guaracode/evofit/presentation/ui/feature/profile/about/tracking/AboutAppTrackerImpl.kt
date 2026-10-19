package com.guaracode.evofit.presentation.ui.feature.profile.about.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class AboutAppTrackerImpl(
    private val crashReporter: CrashReporter
) : AboutAppTracker {

    override fun trackAboutAppScreenView() {
        crashReporter.logEvent("screen_view: profile_about_app")
        crashReporter.setCustomKey("current_screen", "profile_about_app")
    }
}
