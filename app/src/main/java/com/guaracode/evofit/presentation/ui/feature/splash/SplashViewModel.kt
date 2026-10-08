package com.guaracode.evofit.presentation.ui.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guaracode.evofit.data.local.session.SessionManager
import com.guaracode.evofit.domain.usecase.IsOnboardingCompletedUseCase
import com.guaracode.evofit.domain.usecase.SyncUserDataUseCase
import com.guaracode.evofit.navigation.NavRoutes
import com.guaracode.evofit.presentation.ui.feature.splash.tracking.SplashTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
    private val syncUserDataUseCase: SyncUserDataUseCase,
    private val sessionManager: SessionManager,
    private val tracker: SplashTracker
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
                tracker.trackSplashDestinationResolved(
                    destination = NavRoutes.PreLogin.route,
                    isLoggedIn = false,
                    isOnboardingCompleted = false
                )
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

            val targetDestination = if (onboardingCompleted) {
                NavRoutes.Home.route
            } else {
                NavRoutes.Onboarding.route
            }

            tracker.trackSplashDestinationResolved(
                destination = targetDestination,
                isLoggedIn = true,
                isOnboardingCompleted = onboardingCompleted
            )

            _startDestination.value = targetDestination
        }
    }
}
