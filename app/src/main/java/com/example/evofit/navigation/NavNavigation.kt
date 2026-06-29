package com.example.evofit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardUserDataScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingGoalsScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardSummaryScreen
import com.example.evofit.presentation.ui.feature.splash.SplashScreen
import com.example.evofit.presentation.ui.feature.workout.screens.WorkoutScreen

@Composable
fun NavNavigation() {
    val navController = rememberNavController()
    val totalSteps = 4

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen(
                onNavigate = { destination ->
                    navController.navigate(destination) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen(
                currentPage = 0,
                totalPages = totalSteps,
                onFinish = {
                    navController.navigate(NavRoutes.UserData.route)
                }
            )
        }

        composable(NavRoutes.UserData.route) {
            OnboardUserDataScreen(
                currentPage = 1,
                totalPages = totalSteps,
                onContinue = {
                    navController.navigate(NavRoutes.Goals.route)
                }
            )
        }

        composable(NavRoutes.Goals.route) {
            OnboardingGoalsScreen(
                currentPage = 2,
                totalPages = totalSteps,
                onContinue = {
                    navController.navigate(NavRoutes.Summary.route)
                },
                onSkip = {
                    navController.navigate(NavRoutes.Summary.route)
                }
            )
        }

        composable(NavRoutes.Summary.route) {
            OnboardSummaryScreen(
                currentPage = 3,
                totalPages = totalSteps,
                onStartTraining = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                        popUpTo(NavRoutes.Summary.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Home.route) {
            WorkoutScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
    }
}
