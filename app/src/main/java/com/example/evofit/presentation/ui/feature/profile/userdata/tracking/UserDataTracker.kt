package com.example.evofit.presentation.ui.feature.profile.userdata.tracking

interface UserDataTracker {
    fun trackUserDataScreenView()
    fun trackUserDataUpdated(weightChanged: Boolean)
}
