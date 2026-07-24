package com.example.evofit.navigation

import com.example.evofit.core.common.AppConstants

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object PreLogin : NavRoutes("pre_login")
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object RecoverPassword : NavRoutes("recover_password")
    object VerifyCode : NavRoutes("verify_code/{email}") {
        fun createRoute(email: String) = "verify_code/$email"
    }
    object NewPassword : NavRoutes("new_password/{code}") {
        fun createRoute(code: String) = "new_password/$code"
    }
    object Onboarding : NavRoutes("onboarding")
    object Welcome : NavRoutes("welcome")
    object UserData : NavRoutes("user_data")
    object Weight : NavRoutes("weight")
    object Height : NavRoutes("height")
    object Goals : NavRoutes("goals")
    object Summary : NavRoutes("summary")
    object Home : NavRoutes("home")
    object Evo : NavRoutes("evo")
    object Profile : NavRoutes("profile")
    object ProfileUserData : NavRoutes("profile_user_data")
    object ProfilePersonalGoals : NavRoutes("profile_personal_goals")
    
    // Analytics Graph
    object AnalyticsGraph : NavRoutes("analytics_graph")
    object MuscleGroupSelection : NavRoutes("muscle_group_selection")
    object ExerciseSelection : NavRoutes("exercise_selection")
    object ExerciseDetailAnalytics : NavRoutes("exercise_detail_analytics")

    object NewWorkout : NavRoutes("new_workout?editWorkoutId={editWorkoutId}") {
        fun createRoute(editWorkoutId: String? = null) =
            "new_workout?editWorkoutId=${editWorkoutId ?: AppConstants.INVALID_ID}"
    }
    object SelectExercises : NavRoutes("select_exercises/{muscleGroupIds}?editWorkoutId={editWorkoutId}") {
        fun createRoute(muscleGroupIds: String, editWorkoutId: String? = null) =
            "select_exercises/$muscleGroupIds?editWorkoutId=${editWorkoutId ?: AppConstants.INVALID_ID}"
    }
    object ConfigureWorkout : NavRoutes("configure_workout/{exerciseIds}/{workoutName}?editWorkoutId={editWorkoutId}") {
        fun createRoute(exerciseIds: String, workoutName: String, editWorkoutId: String? = null) =
            "configure_workout/$exerciseIds/$workoutName?editWorkoutId=${editWorkoutId ?: AppConstants.INVALID_ID}"
    }
    object WorkoutPreview : NavRoutes("workout_preview/{workoutId}") {
        fun createRoute(workoutId: String) = "workout_preview/$workoutId"
    }
    object WorkoutStart : NavRoutes("workout_start/{workoutId}") {
        fun createRoute(workoutId: String) = "workout_start/$workoutId"
    }
    object WorkoutResume : NavRoutes("workout_resume?workoutId={workoutId}&workoutDoneId={workoutDoneId}&editWorkoutId={editWorkoutId}&workoutNotFinishedId={workoutNotFinishedId}") {
        fun createRoute(
            workoutId: String? = null,
            workoutDoneId: String? = null,
            editWorkoutId: String? = null,
            workoutNotFinishedId: String? = null
        ) = "workout_resume?workoutId=${workoutId ?: AppConstants.INVALID_ID}&workoutDoneId=${workoutDoneId ?: AppConstants.INVALID_ID}&editWorkoutId=${editWorkoutId ?: AppConstants.INVALID_ID}&workoutNotFinishedId=${workoutNotFinishedId ?: AppConstants.INVALID_ID}"
    }
}
