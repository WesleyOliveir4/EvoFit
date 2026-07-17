package com.example.evofit.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.emptyPreferences
import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.model.WorkoutSession
import com.example.evofit.domain.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "workout_session_prefs")

class WorkoutSessionRepositoryImpl(private val context: Context) : WorkoutSessionRepository {

    private val sessionKey = stringPreferencesKey("active_session")

    override fun getActiveSession(): Flow<WorkoutSession?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val json = preferences[sessionKey] ?: return@map null
                runCatching { Json.decodeFromString<WorkoutSession>(json) }.getOrNull()
            }
    }

    override suspend fun startSession(workoutId: String, startTimeMillis: Long) {
        val session = WorkoutSession(
            workoutId = workoutId,
            startTime = startTimeMillis,
            completedSets = emptyList()
        )
        saveSession(session)
    }

    override suspend fun updateCompletedSets(completedSets: List<CompletedSet>) {
        val current = getActiveSession().first() ?: return
        saveSession(current.copy(completedSets = completedSets))
    }

    override suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(sessionKey)
        }
    }

    private suspend fun saveSession(session: WorkoutSession) {
        context.dataStore.edit { preferences ->
            preferences[sessionKey] = Json.encodeToString(session)
        }
    }
}
