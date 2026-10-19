package com.guaracode.evofit.presentation.ui.feature.profile.userdata.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class UserDataTrackerImpl(
    private val crashReporter: CrashReporter
) : UserDataTracker {

    override fun trackUserDataScreenView() {
        crashReporter.logEvent("screen_view: profile_user_data")
        crashReporter.setCustomKey("current_screen", "profile_user_data")
    }

    override fun trackUserDataUpdated(weightChanged: Boolean) {
        crashReporter.logEvent("profile_user_data_updated (weight_changed=$weightChanged)")
    }
}
