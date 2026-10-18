package com.guaracode.evofit.presentation.ui.feature.authentication.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class AuthTrackerImpl(
    private val crashReporter: CrashReporter
) : AuthTracker {

    override fun trackPreLoginScreenView() {
        crashReporter.logEvent("screen_view: auth_pre_login")
        crashReporter.setCustomKey("current_screen", "auth_pre_login")
    }

    override fun trackLoginScreenView() {
        crashReporter.logEvent("screen_view: auth_login")
        crashReporter.setCustomKey("current_screen", "auth_login")
    }

    override fun trackRegisterScreenView() {
        crashReporter.logEvent("screen_view: auth_register")
        crashReporter.setCustomKey("current_screen", "auth_register")
    }

    override fun trackForgotPasswordScreenView() {
        crashReporter.logEvent("screen_view: auth_forgot_password")
        crashReporter.setCustomKey("current_screen", "auth_forgot_password")
    }

    override fun trackRecoverPasswordScreenView() {
        crashReporter.logEvent("screen_view: auth_recover_password")
        crashReporter.setCustomKey("current_screen", "auth_recover_password")
    }

    override fun trackLoginClicked(method: String) {
        crashReporter.logEvent("ui_click: login_button (method=$method)")
    }

    override fun trackRegisterClicked() {
        crashReporter.logEvent("ui_click: register_button")
    }

    override fun trackForgotPasswordClicked() {
        crashReporter.logEvent("ui_click: forgot_password_button")
    }

    override fun trackTermsClicked() {
        crashReporter.logEvent("ui_click: terms_and_conditions_button")
    }

    override fun trackLoginSuccess(method: String) {
        crashReporter.logEvent("auth_login_success (method=$method)")
    }

    override fun trackLoginFailure(method: String, errorMessage: String?) {
        crashReporter.logEvent("auth_login_failure (method=$method, error=${errorMessage ?: "unknown"})")
    }

    override fun trackRegisterSuccess() {
        crashReporter.logEvent("auth_register_success")
    }

    override fun trackRegisterFailure(errorMessage: String?) {
        crashReporter.logEvent("auth_register_failure (error=${errorMessage ?: "unknown"})")
    }

    override fun trackPasswordResetRequested() {
        crashReporter.logEvent("auth_password_reset_requested")
    }

    override fun trackPasswordResetFailure(errorMessage: String?) {
        crashReporter.logEvent("auth_password_reset_failure (error=${errorMessage ?: "unknown"})")
    }
}
