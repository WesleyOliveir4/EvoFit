package com.example.evofit.presentation.ui.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val syncUserDataUseCase: SyncUserDataUseCase,
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
            val userId = sessionManager.userId.firstOrNull()
            
            if (!isLoggedIn || userId == null) {
                _startDestination.value = NavRoutes.PreLogin.route
                return@launch
            }

            var onboardingCompleted = isOnboardingCompletedUseCase().first()
            
            // Se localmente for false, tenta sincronizar uma vez para garantir (Offline First com Cloud Check)
            if (!onboardingCompleted) {
                try {
                    syncUserDataUseCase(userId, shouldClearActiveSession = false, isOnline = true)
                    onboardingCompleted = isOnboardingCompletedUseCase().first()
                } catch (e: Exception) {
                    // Se falhar o sync, mantém o valor local
                }
            }

            _startDestination.value = if (onboardingCompleted) {
                NavRoutes.Home.route
            } else {
                NavRoutes.Onboarding.route
            }
        }
    }
}
