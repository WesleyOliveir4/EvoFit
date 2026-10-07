package com.example.evofit.presentation.ui.feature.profile.support.tracking

interface SupportTracker {
    fun trackSupportScreenView()
    fun trackFAQScreenView()
    fun trackSuggestionScreenView()
    fun trackSupportEmailScreenView()
    fun trackSupportEmailSent(topic: String)
}
