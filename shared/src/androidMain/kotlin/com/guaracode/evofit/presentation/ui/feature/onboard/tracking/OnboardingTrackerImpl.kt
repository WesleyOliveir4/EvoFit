package com.guaracode.evofit.presentation.ui.feature.onboard.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class OnboardingTrackerImpl(
    private val crashReporter: CrashReporter
) : OnboardingTracker {

    override fun trackOnboardingWelcomeScreenView() {
        crashReporter.logEvent("screen_view: onboard_welcome")
        crashReporter.setCustomKey("current_screen", "onboard_welcome")
    }

    override fun trackOnboardingUserDataScreenView() {
        crashReporter.logEvent("screen_view: onboard_user_data")
        crashReporter.setCustomKey("current_screen", "onboard_user_data")
    }

    override fun trackOnboardingWeightScreenView() {
        crashReporter.logEvent("screen_view: onboard_weight")
        crashReporter.setCustomKey("current_screen", "onboard_weight")
    }

    override fun trackOnboardingHeightScreenView() {
        crashReporter.logEvent("screen_view: onboard_height")
        crashReporter.setCustomKey("current_screen", "onboard_height")
    }

    override fun trackOnboardingGoalsScreenView() {
        crashReporter.logEvent("screen_view: onboard_goals")
        crashReporter.setCustomKey("current_screen", "onboard_goals")
    }

    override fun trackOnboardingSummaryScreenView() {
        crashReporter.logEvent("screen_view: onboard_summary")
        crashReporter.setCustomKey("current_screen", "onboard_summary")
    }

    override fun trackGoalAdded(goalType: String) {
        crashReporter.logEvent("onboard_goal_added: $goalType")
    }

    override fun trackGoalRemoved(goalId: String) {
        crashReporter.logEvent("onboard_goal_removed (id=$goalId)")
    }

    override fun trackOnboardingCompleted(goalsCount: Int) {
        crashReporter.logEvent("onboard_completed (goals_count=$goalsCount)")
    }
}
