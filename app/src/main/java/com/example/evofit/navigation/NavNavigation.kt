package com.example.evofit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evofit.presentation.ui.feature.home.screens.HomeScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardUserDataScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingGoalsScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingScreen

@Composable
fun NavNavigation() {
    val navController = rememberNavController()
    val totalSteps = 3

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Onboarding.route
    ) {
        composable(NavRoutes.Onboarding.route) {
            _root_ide_package_.com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingScreen(
                currentPage = 0,
                totalPages = totalSteps,
                onFinish = {
                    navController.navigate(NavRoutes.UserData.route)
                }
            )
        }

        composable(NavRoutes.UserData.route) {
            _root_ide_package_.com.example.evofit.presentation.ui.feature.onboard.screens.OnboardUserDataScreen(
                currentPage = 1,
                totalPages = totalSteps,
                onContinue = {
                    navController.navigate(NavRoutes.Goals.route)
                }
            )
        }

        composable(NavRoutes.Goals.route) {
            _root_ide_package_.com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingGoalsScreen(
                currentPage = 2,
                totalPages = totalSteps,
                onContinue = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Home.route) {
            _root_ide_package_.com.example.evofit.presentation.ui.feature.home.screens.HomeScreen()
        }
    }
}
