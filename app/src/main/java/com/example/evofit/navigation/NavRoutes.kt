package com.example.evofit.navigation

sealed class NavRoutes(val route: String) {
    object Onboarding : NavRoutes("onboarding")
    object UserData : NavRoutes("user_data")
    object Goals : NavRoutes("goals")
    object Home : NavRoutes("home")
}
