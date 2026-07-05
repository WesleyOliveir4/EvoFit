package com.example.evofit.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.model.WorkoutSession
import com.example.evofit.domain.repository.WorkoutSessionRepository
import kotlinx.serialization.json.Json
//TODO
class WorkoutSessionRepositoryImpl(context: Context) : WorkoutSessionRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getActiveSession(): WorkoutSession? {
        val json = prefs.getString(KEY_SESSION, null) ?: return null
        return runCatching { Json.decodeFromString<WorkoutSession>(json) }.getOrNull()
    }

    override fun startSession(workoutId: Long, startTimeMillis: Long) {
        saveSession(
            WorkoutSession(
                workoutId = workoutId,
                startTime = startTimeMillis,
                completedSets = emptyList()
            )
        )
    }

    override fun updateCompletedSets(completedSets: List<CompletedSet>) {
        val current = getActiveSession() ?: return
        saveSession(current.copy(completedSets = completedSets))
    }

    override fun clearSession() {
        prefs.edit { remove(KEY_SESSION) }
    }

    private fun saveSession(session: WorkoutSession) {
        prefs.edit { putString(KEY_SESSION, Json.encodeToString(session)) }
    }

    companion object {
        private const val PREFS_NAME = "workout_session_prefs"
        private const val KEY_SESSION = "active_session"
    }
}
