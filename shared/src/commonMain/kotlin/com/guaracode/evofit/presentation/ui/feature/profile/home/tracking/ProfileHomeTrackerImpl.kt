package com.guaracode.evofit.presentation.ui.feature.profile.home.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class ProfileHomeTrackerImpl(
    private val crashReporter: CrashReporter
) : ProfileHomeTracker {

    override fun trackProfileHomeScreenView() {
        crashReporter.logEvent("screen_view: profile_home")
        crashReporter.setCustomKey("current_screen", "profile_home")
    }

    override fun trackProfileMenuItemClicked(menuItem: String) {
        crashReporter.logEvent("ui_click: profile_menu_$menuItem")
    }

    override fun trackProfilePictureUpdated() {
        crashReporter.logEvent("profile_picture_updated")
    }

    override fun trackLogoutClicked() {
        crashReporter.logEvent("ui_click: profile_logout")
    }
}
