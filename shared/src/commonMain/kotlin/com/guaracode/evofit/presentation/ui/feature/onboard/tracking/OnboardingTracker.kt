package com.guaracode.evofit.presentation.ui.feature.onboard.tracking

interface OnboardingTracker {
    fun trackOnboardingWelcomeScreenView()
    fun trackOnboardingUserDataScreenView()
    fun trackOnboardingWeightScreenView()
    fun trackOnboardingHeightScreenView()
    fun trackOnboardingGoalsScreenView()
    fun trackOnboardingSummaryScreenView()

    fun trackGoalAdded(goalType: String)
    fun trackGoalRemoved(goalId: String)
    fun trackOnboardingCompleted(goalsCount: Int)
}
