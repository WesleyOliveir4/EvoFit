package com.example.evofit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.evofit.core.common.AppConstants
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardUserDataScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingGoalsScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardSummaryScreen
import com.example.evofit.presentation.ui.feature.splash.SplashScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.ConfigureWorkoutScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.NewWorkoutScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.SelectExercisesScreen
import com.example.evofit.presentation.ui.feature.workout.resume.screens.WorkoutResumeScreen
import com.example.evofit.presentation.ui.feature.workout.startworkout.screens.WorkoutPreviewScreen
import com.example.evofit.presentation.ui.feature.workout.startworkout.screens.WorkoutStartScreen
import com.example.evofit.presentation.ui.feature.workout.home.screens.WorkoutScreen
import com.example.evofit.presentation.ui.feature.evo.home.screen.EvoHomeScreen
import com.example.evofit.presentation.ui.feature.evo.analytics.screen.MuscleGroupSelectionScreen

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

        composable(NavRoutes.Evo.route) {
            EvoHomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(NavRoutes.MuscleGroupSelection.route) {
            MuscleGroupSelectionScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onGroupSelected = { muscleGroup ->
                    // TODO: Navegar para os exercícios desse grupo muscular
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
            arguments = listOf(
                navArgument("muscleGroupId") { type = NavType.StringType },
                navArgument("editWorkoutId") { type = NavType.LongType; defaultValue = AppConstants.INVALID_ID }
            )
        ) { backStackEntry ->
            val muscleGroupId = backStackEntry.arguments?.getString("muscleGroupId") ?: ""
            val editWorkoutId = backStackEntry.arguments?.getLong("editWorkoutId")?.takeIf { it != AppConstants.INVALID_ID }
            SelectExercisesScreen(
                muscleGroupId = muscleGroupId,
                editWorkoutId = editWorkoutId,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onConfigureExercisesClick = { exerciseIds, workoutName, editId ->
                    val idsParam = exerciseIds.joinToString(",")
                    navController.navigate(NavRoutes.ConfigureWorkout.createRoute(idsParam, workoutName, editId))
                }
            )
        }

        composable(
            route = NavRoutes.ConfigureWorkout.route,
            arguments = listOf(
                navArgument("exerciseIds") { type = NavType.StringType },
                navArgument("workoutName") { type = NavType.StringType },
                navArgument("editWorkoutId") { type = NavType.LongType; defaultValue = AppConstants.INVALID_ID }
            )
        ) { backStackEntry ->
            val exerciseIds = backStackEntry.arguments?.getString("exerciseIds")?.split(",") ?: emptyList()
            val workoutName = backStackEntry.arguments?.getString("workoutName") ?: ""
            val editWorkoutId = backStackEntry.arguments?.getLong("editWorkoutId")?.takeIf { it != AppConstants.INVALID_ID }
            ConfigureWorkoutScreen(
                exerciseIds = exerciseIds,
                workoutName = workoutName,
                editWorkoutId = editWorkoutId,
                onBackClick = {
                    navController.popBackStack()
                },
                onFinishClick = { workoutId ->
                    navController.navigate(NavRoutes.WorkoutResume.createRoute(workoutId = workoutId)) {
                        popUpTo(NavRoutes.Home.route) { inclusive = false }
                    }
                },
                onFinishEditClick = { workoutId ->
                    navController.navigate(NavRoutes.WorkoutResume.createRoute(editWorkoutId = workoutId)) {
                        popUpTo(NavRoutes.Home.route) { inclusive = false }
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
                },
                onEditClick = { muscleGroupId, editWorkoutId ->
                    navController.navigate(NavRoutes.SelectExercises.createRoute(muscleGroupId, editWorkoutId))
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
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onFinishWorkoutClick = { workoutDoneId ->
                    navController.navigate(NavRoutes.WorkoutResume.createRoute(workoutDoneId = workoutDoneId)) {
                        popUpTo(NavRoutes.Home.route) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.WorkoutResume.route,
            arguments = listOf(
                navArgument("workoutId") { type = NavType.LongType; defaultValue = AppConstants.INVALID_ID },
                navArgument("workoutDoneId") { type = NavType.LongType; defaultValue = AppConstants.INVALID_ID },
                navArgument("editWorkoutId") { type = NavType.LongType; defaultValue = AppConstants.INVALID_ID }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getLong("workoutId")?.takeIf { it != AppConstants.INVALID_ID }
            val workoutDoneId = backStackEntry.arguments?.getLong("workoutDoneId")?.takeIf { it != AppConstants.INVALID_ID }
            val editWorkoutId = backStackEntry.arguments?.getLong("editWorkoutId")?.takeIf { it != AppConstants.INVALID_ID }
            
            WorkoutResumeScreen(
                workoutId = workoutId,
                workoutDoneId = workoutDoneId,
                editWorkoutId = editWorkoutId,
                onContinueClick = {
                    when {
                        workoutDoneId != null -> {
                            navController.navigate(NavRoutes.Home.route) {
                                popUpTo(NavRoutes.Home.route) { inclusive = true }
                            }
                        }
                        editWorkoutId != null -> {
                            navController.navigate(NavRoutes.WorkoutPreview.createRoute(editWorkoutId.toInt())) {
                                popUpTo(NavRoutes.Home.route) { inclusive = false }
                            }
                        }
                        workoutId != null -> {
                            navController.navigate(NavRoutes.WorkoutPreview.createRoute(workoutId.toInt())) {
                                popUpTo(NavRoutes.Home.route) { inclusive = false }
                            }
                        }
                        else -> {
                            navController.navigate(NavRoutes.Home.route) {
                                popUpTo(NavRoutes.Home.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
    }
}
