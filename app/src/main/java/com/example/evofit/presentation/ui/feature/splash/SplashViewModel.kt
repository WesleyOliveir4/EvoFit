package com.example.evofit.presentation.ui.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.core.network.ConnectivityObserver
import com.example.evofit.data.local.session.SessionManager
import com.example.evofit.domain.usecase.IsOnboardingCompletedUseCase
import com.example.evofit.domain.usecase.SyncUserDataUseCase
import com.example.evofit.navigation.NavRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
    private val sessionManager: SessionManager,
    private val connectivityObserver: ConnectivityObserver,
    private val syncUserDataUseCase: SyncUserDataUseCase
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

            val userId = sessionManager.userId.firstOrNull()
            val networkStatus = connectivityObserver.observe().first()
            val isOnline = networkStatus == ConnectivityObserver.Status.Available

            if (userId != null && isOnline) {
                syncUserDataUseCase(userId, shouldClearActiveSession = false, isOnline = true)
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
