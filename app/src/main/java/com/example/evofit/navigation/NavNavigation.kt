package com.example.evofit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evofit.ui.feature.onboard.screens.OnboardUserDataScreen
import com.example.evofit.ui.feature.onboard.screens.OnboardingGoalsScreen
import com.example.evofit.ui.feature.onboard.screens.OnboardingScreen

@Composable
fun NavNavigation() {
    val navController = rememberNavController()

    var userName by remember { mutableStateOf("") }
    var userAge by remember { mutableStateOf("") }
    var userWeight by remember { mutableStateOf("") }

    val totalSteps = 3

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Onboarding.route
    ) {
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
                name = userName,
                age = userAge,
                weight = userWeight,
                currentPage = 1,
                totalPages = totalSteps,
                onNameChange = { userName = it },
                onAgeChange = { userAge = it },
                onWeightChange = { userWeight = it },
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
                    navController.navigate(NavRoutes.Home.route)
                },
                onSkip = {
                    navController.navigate(NavRoutes.Home.route)
                }
            )
        }

        composable(NavRoutes.Home.route) {
            // Placeholder para a Home
        }
    }
}
