package com.guaracode.evofit.presentation.ui.feature.profile.about.viewmodel

import androidx.lifecycle.ViewModel
import com.guaracode.evofit.domain.usecase.GetAppVersionUseCase
import com.guaracode.evofit.presentation.ui.feature.profile.about.state.AboutAppUiState
import com.guaracode.evofit.presentation.ui.feature.profile.about.tracking.AboutAppTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AboutAppViewModel(
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val tracker: AboutAppTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutAppUiState(appVersion = getAppVersionUseCase()))
    val uiState: StateFlow<AboutAppUiState> = _uiState.asStateFlow()

    fun trackScreenView() {
        tracker.trackAboutAppScreenView()
    }
}
