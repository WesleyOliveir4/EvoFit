package com.guaracode.evofit.presentation.ui.feature.profile.goals.tracking

interface PersonalGoalsTracker {
    fun trackPersonalGoalsScreenView()
    fun trackGoalAdded(goalType: String)
    fun trackGoalDeleted(goalId: String)
}
