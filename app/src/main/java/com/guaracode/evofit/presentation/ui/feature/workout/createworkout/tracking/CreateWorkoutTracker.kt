package com.guaracode.evofit.presentation.ui.feature.workout.createworkout.tracking

interface CreateWorkoutTracker {
    fun trackNewWorkoutScreenView()
    fun trackSelectExercisesScreenView()
    fun trackConfigureWorkoutScreenView()
    fun trackWorkoutCreated(workoutName: String, exercisesCount: Int)
    fun trackExerciseAddedToWorkout(exerciseName: String)
    fun trackExerciseRemovedFromWorkout(exerciseName: String)
}
