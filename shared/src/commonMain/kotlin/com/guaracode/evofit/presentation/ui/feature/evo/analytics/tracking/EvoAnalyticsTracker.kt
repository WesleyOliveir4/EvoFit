package com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking

interface EvoAnalyticsTracker {
    fun trackCategoryAnalyticsSelectionScreenView()
    fun trackExerciseSelectionScreenView(muscleGroupName: String)
    fun trackCategoryDetailAnalyticsScreenView(exerciseName: String)
    fun trackMuscleGroupSelected(groupId: String, groupName: String)
    fun trackWeightCategorySelected()
    fun trackExerciseSelected(exerciseId: String, exerciseName: String)
}
