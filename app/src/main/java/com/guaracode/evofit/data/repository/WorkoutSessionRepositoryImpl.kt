package com.guaracode.evofit.data.repository

import com.guaracode.evofit.data.datasource.WorkoutLocalDataSource
import com.guaracode.evofit.data.local.entities.ActiveSessionEntity
import com.guaracode.evofit.data.mapper.toDomain
import com.guaracode.evofit.data.mapper.toEntity
import com.guaracode.evofit.domain.model.CompletedSet
import com.guaracode.evofit.domain.model.WorkoutSession
import com.guaracode.evofit.domain.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class WorkoutSessionRepositoryImpl(
    private val workoutDataSource: WorkoutLocalDataSource
) : WorkoutSessionRepository {

    override fun getActiveSession(): Flow<WorkoutSession?> {
        return workoutDataSource.getActiveSession().map { activeSessionWithSets ->
            activeSessionWithSets?.let {
                it.session.toDomain(it.sets)
            }
        }
    }

    override suspend fun startSession(workoutId: String, startTimeMillis: Long) {
        val sessionEntity = ActiveSessionEntity(
            workoutId = workoutId,
            startTime = startTimeMillis
        )
        workoutDataSource.insertActiveSession(sessionEntity, emptyList())
    }

    override suspend fun updateCompletedSets(completedSets: List<CompletedSet>) {
        val currentSession = workoutDataSource.getActiveSession().first()?.session ?: return
        val setEntities = completedSets.map { it.toEntity(currentSession.workoutId) }
        
        workoutDataSource.insertActiveSession(currentSession, setEntities)
    }

    override suspend fun clearSession() {
        workoutDataSource.deleteActiveSession()
    }
}
