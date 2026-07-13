package com.example.evofit.navigation

import com.example.evofit.core.common.AppConstants

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")
    object UserData : NavRoutes("user_data")
    object Goals : NavRoutes("goals")
    object Summary : NavRoutes("summary")
    object Home : NavRoutes("home")
    object Evo : NavRoutes("evo")
    
    // Analytics Graph
    object AnalyticsGraph : NavRoutes("analytics_graph")
    object MuscleGroupSelection : NavRoutes("muscle_group_selection")
    object ExerciseSelection : NavRoutes("exercise_selection")
    object ExerciseDetailAnalytics : NavRoutes("exercise_detail_analytics")

    object NewWorkout : NavRoutes("new_workout")
    object SelectExercises : NavRoutes("select_exercises/{muscleGroupId}?editWorkoutId={editWorkoutId}") {
        fun createRoute(muscleGroupId: String, editWorkoutId: Long? = null) =
            "select_exercises/$muscleGroupId?editWorkoutId=${editWorkoutId ?: AppConstants.INVALID_ID}"
    }
    object ConfigureWorkout : NavRoutes("configure_workout/{exerciseIds}/{workoutName}?editWorkoutId={editWorkoutId}") {
        fun createRoute(exerciseIds: String, workoutName: String, editWorkoutId: Long? = null) =
            "configure_workout/$exerciseIds/$workoutName?editWorkoutId=${editWorkoutId ?: AppConstants.INVALID_ID}"
    }
    object WorkoutPreview : NavRoutes("workout_preview/{workoutId}") {
        fun createRoute(workoutId: Int) = "workout_preview/$workoutId"
    }
    object WorkoutStart : NavRoutes("workout_start/{workoutId}") {
        fun createRoute(workoutId: Int) = "workout_start/$workoutId"
    }
    object WorkoutResume : NavRoutes("workout_resume?workoutId={workoutId}&workoutDoneId={workoutDoneId}&editWorkoutId={editWorkoutId}") {
        fun createRoute(workoutId: Long? = null, workoutDoneId: Long? = null, editWorkoutId: Long? = null) =
            "workout_resume?workoutId=${workoutId ?: AppConstants.INVALID_ID}&workoutDoneId=${workoutDoneId ?: AppConstants.INVALID_ID}&editWorkoutId=${editWorkoutId ?: AppConstants.INVALID_ID}"
    }
}
