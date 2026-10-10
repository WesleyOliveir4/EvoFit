package com.guaracode.evofit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.guaracode.evofit.presentation.ui.feature.onboard.screens.*

sealed interface OnboardingScreenDest {
    object Welcome : OnboardingScreenDest
    object UserData : OnboardingScreenDest
    object Weight : OnboardingScreenDest
    object Height : OnboardingScreenDest
    object Goals : OnboardingScreenDest
    object Summary : OnboardingScreenDest
}

@Composable
fun OnboardingNavigation(
    onFinishOnboarding: () -> Unit = {}
) {
    var currentScreen by remember { mutableStateOf<OnboardingScreenDest>(OnboardingScreenDest.Welcome) }
    val totalPages = 6

    when (currentScreen) {
        is OnboardingScreenDest.Welcome -> {
            OnboardingScreen(
                currentPage = 0,
                totalPages = totalPages,
                onFinish = { currentScreen = OnboardingScreenDest.UserData }
            )
        }
        is OnboardingScreenDest.UserData -> {
            OnboardUserDataScreen(
                currentPage = 1,
                totalPages = totalPages,
                onContinue = { currentScreen = OnboardingScreenDest.Weight },
                onBack = { currentScreen = OnboardingScreenDest.Welcome }
            )
        }
        is OnboardingScreenDest.Weight -> {
            OnboardWeightScreen(
                currentPage = 2,
                totalPages = totalPages,
                onContinue = { currentScreen = OnboardingScreenDest.Height },
                onBack = { currentScreen = OnboardingScreenDest.UserData }
            )
        }
        is OnboardingScreenDest.Height -> {
            OnboardHeightScreen(
                currentPage = 3,
                totalPages = totalPages,
                onContinue = { currentScreen = OnboardingScreenDest.Goals },
                onBack = { currentScreen = OnboardingScreenDest.Weight }
            )
        }
        is OnboardingScreenDest.Goals -> {
            OnboardingGoalsScreen(
                currentPage = 4,
                totalPages = totalPages,
                onContinue = { currentScreen = OnboardingScreenDest.Summary },
                onSkip = { currentScreen = OnboardingScreenDest.Summary },
                onBack = { currentScreen = OnboardingScreenDest.Height }
            )
        }
        is OnboardingScreenDest.Summary -> {
            OnboardSummaryScreen(
                currentPage = 5,
                totalPages = totalPages,
                onStartTraining = onFinishOnboarding,
                onBack = { currentScreen = OnboardingScreenDest.Goals }
            )
        }
    }
}
