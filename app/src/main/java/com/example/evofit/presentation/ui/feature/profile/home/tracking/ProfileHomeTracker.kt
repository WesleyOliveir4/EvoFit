package com.example.evofit.presentation.ui.feature.profile.home.tracking

interface ProfileHomeTracker {
    fun trackProfileHomeScreenView()
    fun trackProfileMenuItemClicked(menuItem: String)
    fun trackProfilePictureUpdated()
    fun trackLogoutClicked()
}
