package com.guaracode.evofit.presentation.ui.feature.profile.developer.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class DeveloperTrackerImpl(
    private val crashReporter: CrashReporter
) : DeveloperTracker {

    override fun trackDeveloperScreenView() {
        crashReporter.logEvent("screen_view: profile_developer")
        crashReporter.setCustomKey("current_screen", "profile_developer")
    }

    override fun trackGenerateFakeHistoryClicked() {
        crashReporter.logEvent("ui_click: dev_generate_fake_history")
    }
}
