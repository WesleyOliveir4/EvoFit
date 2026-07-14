package com.example.evofit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.evofit.core.common.AppConstants
import com.example.evofit.presentation.ui.feature.evo.analytics.screen.ExerciseDetailAnalyticsScreen
import com.example.evofit.presentation.ui.feature.evo.analytics.screen.ExerciseSelectionScreen
import com.example.evofit.presentation.ui.feature.evo.analytics.screen.MuscleGroupSelectionScreen
import com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.example.evofit.presentation.ui.feature.evo.home.screen.EvoHomeScreen
import com.example.evofit.presentation.ui.feature.login.screens.LoginScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardSummaryScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardUserDataScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingGoalsScreen
import com.example.evofit.presentation.ui.feature.onboard.screens.OnboardingScreen
import com.example.evofit.presentation.ui.feature.profile.home.screens.ProfileHomeScreen
import com.example.evofit.presentation.ui.feature.profile.goals.screens.PersonalGoalsScreen
import com.example.evofit.presentation.ui.feature.profile.userdata.screens.UserDataScreen
import com.example.evofit.presentation.ui.feature.profile.userdata.viewmodel.UserDataViewModel
import com.example.evofit.presentation.ui.feature.profile.home.viewmodel.ProfileViewModel
import com.example.evofit.presentation.ui.feature.splash.SplashScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.ConfigureWorkoutScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.NewWorkoutScreen
import com.example.evofit.presentation.ui.feature.workout.createworkout.screens.SelectExercisesScreen
import com.example.evofit.presentation.ui.feature.workout.home.screens.WorkoutScreen
import com.example.evofit.presentation.ui.feature.workout.resume.screens.WorkoutResumeScreen
import com.example.evofit.presentation.ui.feature.workout.startworkout.screens.WorkoutPreviewScreen
import com.example.evofit.presentation.ui.feature.workout.startworkout.screens.WorkoutStartScreen
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
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

        composable(NavRoutes.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    // A lógica de login viria aqui (ViewModel)
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(NavRoutes.Onboarding.route)
                },
                onForgotPasswordClick = {
                    // Navegar para recuperar senha
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
                    if (route == NavRoutes.MuscleGroupSelection.route) {
                        navController.navigate(NavRoutes.AnalyticsGraph.route)
                    } else {
                        navController.navigate(route)
                    }
                }
            )
        }

        navigation(
            startDestination = NavRoutes.MuscleGroupSelection.route,
            route = NavRoutes.AnalyticsGraph.route
        ) {
            composable(NavRoutes.MuscleGroupSelection.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<EvoAnalyticsViewModel>(navController)
                MuscleGroupSelectionScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onGroupSelected = { _, _ ->
                        navController.navigate(NavRoutes.ExerciseSelection.route)
                    }
                )
            }

            composable(NavRoutes.ExerciseSelection.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<EvoAnalyticsViewModel>(navController)
                ExerciseSelectionScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onExerciseClick = { _ ->
                        navController.navigate(NavRoutes.ExerciseDetailAnalytics.route)
                    }
                )
            }

            composable(NavRoutes.ExerciseDetailAnalytics.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<EvoAnalyticsViewModel>(navController)
                ExerciseDetailAnalyticsScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
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

        composable(NavRoutes.Profile.route) {
            ProfileHomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onUserDataClick = {
                    navController.navigate(NavRoutes.ProfileUserData.route)
                },
                onGoalsClick = {
                    navController.navigate(NavRoutes.ProfilePersonalGoals.route)
                }
            )
        }

        composable(NavRoutes.ProfilePersonalGoals.route) {
            PersonalGoalsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.ProfileUserData.route) {
            UserDataScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}
