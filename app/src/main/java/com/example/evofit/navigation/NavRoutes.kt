package com.example.evofit.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")
    object UserData : NavRoutes("user_data")
    object Goals : NavRoutes("goals")
    object Summary : NavRoutes("summary")
    object Home : NavRoutes("home")
    object NewWorkout : NavRoutes("new_workout")
    object SelectExercises : NavRoutes("select_exercises/{muscleGroupId}") {
        fun createRoute(muscleGroupId: String) = "select_exercises/$muscleGroupId"
    }
    object ConfigureWorkout : NavRoutes("configure_workout/{exerciseIds}/{workoutName}") {
        fun createRoute(exerciseIds: String, workoutName: String) = "configure_workout/$exerciseIds/$workoutName"
    }
    object WorkoutPreview : NavRoutes("workout_preview/{workoutId}") {
        fun createRoute(workoutId: Int) = "workout_preview/$workoutId"
    }
}
