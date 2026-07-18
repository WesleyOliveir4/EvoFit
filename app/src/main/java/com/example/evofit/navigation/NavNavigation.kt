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
import com.example.evofit.presentation.ui.feature.authentication.screens.*
import com.example.evofit.presentation.ui.feature.onboard.screens.*
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.feature.profile.home.screens.ProfileHomeScreen
import com.example.evofit.presentation.ui.feature.profile.goals.screens.PersonalGoalsScreen
import com.example.evofit.presentation.ui.feature.profile.userdata.screens.UserDataScreen
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
    val totalSteps = 6

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        // ... (Splash, Login, Register screens)
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
                onLoginSuccess = { isOnboardingCompleted ->
                    val destination = if (isOnboardingCompleted) NavRoutes.Home.route else NavRoutes.Onboarding.route
                    navController.navigate(destination) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(NavRoutes.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(NavRoutes.RecoverPassword.route)
                }
            )
        }

        composable(NavRoutes.RecoverPassword.route) {
            RecoverPasswordScreen(
                onCodeSent = { email ->
                    navController.navigate(NavRoutes.VerifyCode.createRoute(email))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavRoutes.VerifyCode.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyCodeScreen(
                email = email,
                onCodeVerified = { code ->
                    navController.navigate(NavRoutes.NewPassword.createRoute(email, code))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavRoutes.NewPassword.route,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("code") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""
            NewPasswordScreen(
                email = email,
                code = code,
                onPasswordResetSuccess = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.RecoverPassword.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(NavRoutes.Register.route) { inclusive = true }
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        navigation(
            startDestination = NavRoutes.UserData.route,
            route = NavRoutes.Onboarding.route
        ) {
            composable(NavRoutes.UserData.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)
                OnboardUserDataScreen(
                    viewModel = viewModel,
                    currentPage = 1,
                    totalPages = totalSteps,
                    onContinue = {
                        navController.navigate(NavRoutes.Weight.route)
                    }
                )
            }

            composable(NavRoutes.Weight.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)
                OnboardWeightScreen(
                    viewModel = viewModel,
                    currentPage = 2,
                    totalPages = totalSteps,
                    onContinue = {
                        navController.navigate(NavRoutes.Height.route)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavRoutes.Height.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)
                OnboardHeightScreen(
                    viewModel = viewModel,
                    currentPage = 3,
                    totalPages = totalSteps,
                    onContinue = {
                        navController.navigate(NavRoutes.Goals.route)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavRoutes.Goals.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)
                OnboardingGoalsScreen(
                    viewModel = viewModel,
                    currentPage = 4,
                    totalPages = totalSteps,
                    onContinue = {
                        navController.navigate(NavRoutes.Summary.route)
                    },
                    onSkip = {
                        navController.navigate(NavRoutes.Summary.route)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavRoutes.Summary.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)
                OnboardSummaryScreen(
                    viewModel = viewModel,
                    currentPage = 5,
                    totalPages = totalSteps,
                    onStartTraining = {
                        navController.navigate(NavRoutes.Home.route) {
                            popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
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
                navArgument("editWorkoutId") { type = NavType.StringType; defaultValue = AppConstants.INVALID_ID }
            )
        ) { backStackEntry ->
            val muscleGroupId = backStackEntry.arguments?.getString("muscleGroupId") ?: ""
            val editWorkoutId = backStackEntry.arguments?.getString("editWorkoutId")?.takeIf { it != AppConstants.INVALID_ID }
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
                navArgument("editWorkoutId") { type = NavType.StringType; defaultValue = AppConstants.INVALID_ID }
            )
        ) { backStackEntry ->
            val exerciseIds = backStackEntry.arguments?.getString("exerciseIds")?.split(",") ?: emptyList()
            val workoutName = backStackEntry.arguments?.getString("workoutName") ?: ""
            val editWorkoutId = backStackEntry.arguments?.getString("editWorkoutId")?.takeIf { it != AppConstants.INVALID_ID }
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
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
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
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
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
                navArgument("workoutId") { type = NavType.StringType; defaultValue = AppConstants.INVALID_ID },
                navArgument("workoutDoneId") { type = NavType.StringType; defaultValue = AppConstants.INVALID_ID },
                navArgument("editWorkoutId") { type = NavType.StringType; defaultValue = AppConstants.INVALID_ID }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")?.takeIf { it != AppConstants.INVALID_ID }
            val workoutDoneId = backStackEntry.arguments?.getString("workoutDoneId")?.takeIf { it != AppConstants.INVALID_ID }
            val editWorkoutId = backStackEntry.arguments?.getString("editWorkoutId")?.takeIf { it != AppConstants.INVALID_ID }
            
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
                            navController.navigate(NavRoutes.WorkoutPreview.createRoute(editWorkoutId)) {
                                popUpTo(NavRoutes.Home.route) { inclusive = false }
                            }
                        }
                        workoutId != null -> {
                            navController.navigate(NavRoutes.WorkoutPreview.createRoute(workoutId)) {
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
