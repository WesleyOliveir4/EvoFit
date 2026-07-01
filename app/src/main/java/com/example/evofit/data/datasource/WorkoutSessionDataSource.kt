package com.example.evofit.data.datasource

import android.content.Context
import android.content.SharedPreferences

class WorkoutSessionDataSource(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun startSession(workoutId: Int, startTimeMillis: Long) {
        prefs.edit()
            .putInt(KEY_WORKOUT_ID, workoutId)
            .putLong(KEY_START_TIME, startTimeMillis)
            .apply()
    }

    fun getSessionStartTime(workoutId: Int): Long? {
        val activeWorkoutId = prefs.getInt(KEY_WORKOUT_ID, -1)
        return if (activeWorkoutId == workoutId) {
            val startTime = prefs.getLong(KEY_START_TIME, 0L)
            if (startTime > 0) startTime else null
        } else null
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "workout_session_prefs"
        private const val KEY_WORKOUT_ID = "active_workout_id"
        private const val KEY_START_TIME = "workout_start_time"
    }
}
