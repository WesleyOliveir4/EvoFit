package com.example.evofit.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")
    object UserData : NavRoutes("user_data")
    object Goals : NavRoutes("goals")
    object Summary : NavRoutes("summary")
    object Home : NavRoutes("home")
}
