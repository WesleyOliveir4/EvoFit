package com.guaracode.evofit.presentation.ui.feature.profile.goals.tracking

import com.guaracode.evofit.core.monitoring.CrashReporter

class PersonalGoalsTrackerImpl(
    private val crashReporter: CrashReporter
) : PersonalGoalsTracker {

    override fun trackPersonalGoalsScreenView() {
        crashReporter.logEvent("screen_view: profile_personal_goals")
        crashReporter.setCustomKey("current_screen", "profile_personal_goals")
    }

    override fun trackGoalAdded(goalType: String) {
        crashReporter.logEvent("profile_goal_added: $goalType")
    }

    override fun trackGoalDeleted(goalId: String) {
        crashReporter.logEvent("profile_goal_deleted (id=$goalId)")
    }
}
