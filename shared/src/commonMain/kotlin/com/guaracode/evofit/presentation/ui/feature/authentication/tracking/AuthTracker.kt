package com.guaracode.evofit.presentation.ui.feature.authentication.tracking

interface AuthTracker {
    // Screen Views
    fun trackPreLoginScreenView()
    fun trackLoginScreenView()
    fun trackRegisterScreenView()
    fun trackForgotPasswordScreenView()
    fun trackRecoverPasswordScreenView()

    // UI Actions
    fun trackLoginClicked(method: String)
    fun trackRegisterClicked()
    fun trackForgotPasswordClicked()
    fun trackTermsClicked()

    // ViewModel Outcomes
    fun trackLoginSuccess(method: String)
    fun trackLoginFailure(method: String, errorMessage: String?)
    fun trackRegisterSuccess()
    fun trackRegisterFailure(errorMessage: String?)
    fun trackPasswordResetRequested()
    fun trackPasswordResetFailure(errorMessage: String?)
}
