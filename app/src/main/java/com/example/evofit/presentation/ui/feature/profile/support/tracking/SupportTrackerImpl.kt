package com.example.evofit.presentation.ui.feature.profile.support.tracking

import com.example.evofit.core.monitoring.CrashReporter

class SupportTrackerImpl(
    private val crashReporter: CrashReporter
) : SupportTracker {

    override fun trackSupportScreenView() {
        crashReporter.logEvent("screen_view: profile_support")
        crashReporter.setCustomKey("current_screen", "profile_support")
    }

    override fun trackFAQScreenView() {
        crashReporter.logEvent("screen_view: profile_support_faq")
        crashReporter.setCustomKey("current_screen", "profile_support_faq")
    }

    override fun trackSuggestionScreenView() {
        crashReporter.logEvent("screen_view: profile_support_suggestion")
        crashReporter.setCustomKey("current_screen", "profile_support_suggestion")
    }

    override fun trackSupportEmailScreenView() {
        crashReporter.logEvent("screen_view: profile_support_email")
        crashReporter.setCustomKey("current_screen", "profile_support_email")
    }

    override fun trackSupportEmailSent(topic: String) {
        crashReporter.logEvent("support_email_sent (topic=$topic)")
    }
}
