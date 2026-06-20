package com.example.evofit.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "onboarding_prefs")

class OnboardingRepositoryImpl(private val context: Context) : OnboardingRepository {

    private object PreferencesKeys {
        val NAME = stringPreferencesKey("user_name")
        val AGE = stringPreferencesKey("user_age")
        val WEIGHT = stringPreferencesKey("user_weight")
        val HEIGHT = stringPreferencesKey("user_height")
        val GOALS = stringPreferencesKey("user_goals_json")
        val COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    override fun getUserData(): Flow<UserOnboardingData> = context.dataStore.data.map { prefs ->
        val goalsJson = prefs[PreferencesKeys.GOALS]
        val goals = if (!goalsJson.isNullOrEmpty()) {
            try {
                Json.decodeFromString<List<UserGoal>>(goalsJson)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }

        UserOnboardingData(
            name = prefs[PreferencesKeys.NAME] ?: "",
            age = prefs[PreferencesKeys.AGE] ?: "",
            weight = prefs[PreferencesKeys.WEIGHT] ?: "",
            height = prefs[PreferencesKeys.HEIGHT] ?: "",
            goals = goals
        )
    }

    override suspend fun saveUserData(data: UserOnboardingData) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.NAME] = data.name
            prefs[PreferencesKeys.AGE] = data.age
            prefs[PreferencesKeys.WEIGHT] = data.weight
            prefs[PreferencesKeys.HEIGHT] = data.height
            prefs[PreferencesKeys.GOALS] = Json.encodeToString(data.goals)
        }
    }

    override suspend fun completeOnboarding() {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.COMPLETED] = true
        }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.COMPLETED] ?: false
    }
}
