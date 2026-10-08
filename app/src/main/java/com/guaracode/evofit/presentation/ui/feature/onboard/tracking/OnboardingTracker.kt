package com.guaracode.evofit.presentation.ui.feature.onboard.tracking

interface OnboardingTracker {
    // Screen Views
    fun trackOnboardingWelcomeScreenView()
    fun trackOnboardingUserDataScreenView()
    fun trackOnboardingWeightScreenView()
    fun trackOnboardingHeightScreenView()
    fun trackOnboardingGoalsScreenView()
    fun trackOnboardingSummaryScreenView()

    // Actions
    fun trackGoalAdded(goalType: String)
    fun trackGoalRemoved(goalId: String)
    fun trackOnboardingCompleted(goalsCount: Int)
}
