package com.guaracode.evofit.presentation.ui.feature.commons.goals.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class GoalWizardTrackerImpl(
    private val crashReporter: CrashReporter
) : GoalWizardTracker {

    override fun trackGoalWizardOpened(hasSuggestion: Boolean) {
        crashReporter.logEvent("goal_wizard_opened (has_suggestion=$hasSuggestion)")
    }

    override fun trackGoalWizardStepView(stepName: String) {
        crashReporter.logEvent("screen_view: goal_wizard_step_$stepName")
        crashReporter.setCustomKey("current_goal_step", stepName)
    }

    override fun trackCategorySelected(category: String) {
        crashReporter.logEvent("goal_wizard_category_selected: $category")
    }

    override fun trackMuscleGroupSelected(muscleName: String) {
        crashReporter.logEvent("goal_wizard_muscle_selected: $muscleName")
    }

    override fun trackExerciseSelected(exerciseName: String) {
        crashReporter.logEvent("goal_wizard_exercise_selected: $exerciseName")
    }

    override fun trackGoalConfirmed(goalType: String) {
        crashReporter.logEvent("goal_wizard_confirmed: $goalType")
    }

    override fun trackGoalWizardCancelled() {
        crashReporter.logEvent("goal_wizard_cancelled")
    }
}
