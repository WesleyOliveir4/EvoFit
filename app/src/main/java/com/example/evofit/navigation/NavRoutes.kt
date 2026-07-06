package com.example.evofit.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")
    object UserData : NavRoutes("user_data")
    object Goals : NavRoutes("goals")
    object Summary : NavRoutes("summary")
    object Home : NavRoutes("home")
    object NewWorkout : NavRoutes("new_workout")
    object SelectExercises : NavRoutes("select_exercises/{muscleGroupId}?editWorkoutId={editWorkoutId}") {
        fun createRoute(muscleGroupId: String, editWorkoutId: Long? = null) =
            "select_exercises/$muscleGroupId?editWorkoutId=${editWorkoutId ?: -1L}"
    }
    object ConfigureWorkout : NavRoutes("configure_workout/{exerciseIds}/{workoutName}?editWorkoutId={editWorkoutId}") {
        fun createRoute(exerciseIds: String, workoutName: String, editWorkoutId: Long? = null) =
            "configure_workout/$exerciseIds/$workoutName?editWorkoutId=${editWorkoutId ?: -1L}"
    }
    object WorkoutPreview : NavRoutes("workout_preview/{workoutId}") {
        fun createRoute(workoutId: Int) = "workout_preview/$workoutId"
    }
    object WorkoutStart : NavRoutes("workout_start/{workoutId}") {
        fun createRoute(workoutId: Int) = "workout_start/$workoutId"
    }
}
