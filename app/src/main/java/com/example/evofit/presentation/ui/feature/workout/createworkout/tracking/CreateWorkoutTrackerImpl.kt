package com.example.evofit.presentation.ui.feature.workout.createworkout.tracking

import com.example.evofit.core.monitoring.CrashReporter

class CreateWorkoutTrackerImpl(
    private val crashReporter: CrashReporter
) : CreateWorkoutTracker {

    override fun trackNewWorkoutScreenView() {
        crashReporter.logEvent("screen_view: create_workout_new_name")
        crashReporter.setCustomKey("current_screen", "create_workout_new_name")
    }

    override fun trackSelectExercisesScreenView() {
        crashReporter.logEvent("screen_view: create_workout_select_exercises")
        crashReporter.setCustomKey("current_screen", "create_workout_select_exercises")
    }

    override fun trackConfigureWorkoutScreenView() {
        crashReporter.logEvent("screen_view: create_workout_configure")
        crashReporter.setCustomKey("current_screen", "create_workout_configure")
    }

    override fun trackWorkoutCreated(workoutName: String, exercisesCount: Int) {
        crashReporter.logEvent("workout_created: $workoutName (exercises_count=$exercisesCount)")
    }

    override fun trackExerciseAddedToWorkout(exerciseName: String) {
        crashReporter.logEvent("create_workout_exercise_added: $exerciseName")
    }

    override fun trackExerciseRemovedFromWorkout(exerciseName: String) {
        crashReporter.logEvent("create_workout_exercise_removed: $exerciseName")
    }
}
