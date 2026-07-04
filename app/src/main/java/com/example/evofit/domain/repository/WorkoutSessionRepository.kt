package com.example.evofit.domain.repository

/**
 * Abstrai a persistência do estado de um treino em andamento (timer, id ativo).
 * A implementação concreta (SharedPreferences, DataStore, etc.) fica na camada data.
 */
interface WorkoutSessionRepository {
    fun startSession(workoutId: Int, startTimeMillis: Long)
    fun getSessionStartTime(workoutId: Int): Long?
    fun clearSession()
}
