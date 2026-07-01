package com.example.evofit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardUserDataScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingGoalsScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardSummaryScreen
import com.example.evofit.presentation.ui.feature.splash.SplashScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.ConfigureWorkoutScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.NewWorkoutScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.SelectExercisesScreen
import com.example.evofit.presentation.ui.feature.workout.startworkout.screens.WorkoutPreviewScreen
import com.example.evofit.presentation.ui.feature.workout.startworkout.screens.WorkoutStartScreen
import com.example.evofit.presentation.ui.feature.workout.home.screens.WorkoutScreen

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

        composable(NavRoutes.NewWorkout.route) {
            NewWorkoutScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onGroupSelected = { groupId ->
                    navController.navigate(NavRoutes.SelectExercises.createRoute(groupId))
                }
            )
        }

        composable(
            route = NavRoutes.SelectExercises.route,
            arguments = listOf(navArgument("muscleGroupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val muscleGroupId = backStackEntry.arguments?.getString("muscleGroupId") ?: ""
            SelectExercisesScreen(
                muscleGroupId = muscleGroupId,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onConfigureExercisesClick = { exerciseIds, workoutName ->
                    val idsParam = exerciseIds.joinToString(",")
                    navController.navigate(NavRoutes.ConfigureWorkout.createRoute(idsParam, workoutName))
                }
            )
        }

        composable(
            route = NavRoutes.ConfigureWorkout.route,
            arguments = listOf(
                navArgument("exerciseIds") { type = NavType.StringType },
                navArgument("workoutName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val exerciseIds = backStackEntry.arguments?.getString("exerciseIds")?.split(",") ?: emptyList()
            val workoutName = backStackEntry.arguments?.getString("workoutName") ?: ""
            ConfigureWorkoutScreen(
                exerciseIds = exerciseIds,
                workoutName = workoutName,
                onBackClick = {
                    navController.popBackStack()
                },
                onFinishClick = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.WorkoutPreview.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
            WorkoutPreviewScreen(
                workoutId = workoutId,
                onBackClick = {
                    navController.popBackStack()
                },
                onStartWorkoutClick = {
                    navController.navigate(NavRoutes.WorkoutStart.createRoute(workoutId))
                }
            )
        }

        composable(
            route = NavRoutes.WorkoutStart.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
            WorkoutStartScreen(
                workoutId = workoutId,
                onBackClick = {
                    navController.popBackStack()
                },
                onFinishWorkoutClick = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
