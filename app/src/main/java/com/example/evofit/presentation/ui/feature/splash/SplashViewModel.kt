package com.example.evofit.presentation.ui.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.data.local.session.SessionManager
import com.example.evofit.domain.usecase.IsOnboardingCompletedUseCase
import com.example.evofit.navigation.NavRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        checkAppStatus()
    }

    private fun checkAppStatus() {
        viewModelScope.launch {
            val isLoggedIn = sessionManager.isLoggedIn.first()
            
            if (!isLoggedIn) {
                _startDestination.value = NavRoutes.PreLogin.route
                return@launch
            }

            val onboardingCompleted = isOnboardingCompletedUseCase().first()
            _startDestination.value = if (onboardingCompleted) {
                NavRoutes.Home.route
            } else {
                NavRoutes.Onboarding.route
            }
        }
    }
}
