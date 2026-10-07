package com.example.evofit.presentation.ui.feature.commons.goals.tracking

interface GoalWizardTracker {
    fun trackGoalWizardOpened(hasSuggestion: Boolean)
    fun trackGoalWizardStepView(stepName: String)
    fun trackCategorySelected(category: String)
    fun trackMuscleGroupSelected(muscleName: String)
    fun trackExerciseSelected(exerciseName: String)
    fun trackGoalConfirmed(goalType: String)
    fun trackGoalWizardCancelled()
}
