package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.ActiveWorkoutSession
import com.guaracode.evofit.domain.repository.WorkoutRepository
import com.guaracode.evofit.domain.repository.WorkoutSessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface GetActiveWorkoutSessionUseCase {
    operator fun invoke(): Flow<ActiveWorkoutSession?>
}

class GetActiveWorkoutSessionUseCaseImpl(
    private val sessionRepository: WorkoutSessionRepository,
    private val workoutRepository: WorkoutRepository
) : GetActiveWorkoutSessionUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(): Flow<ActiveWorkoutSession?> {
        return sessionRepository.getActiveSession().flatMapLatest { session ->
            if (session == null) return@flatMapLatest flowOf(null)

            workoutRepository.getWorkoutById(session.workoutId).map { workout ->
                ActiveWorkoutSession(
                    workout = workout ?: return@map null,
                    startTime = session.startTime,
                    completedSets = session.completedSets
                )
            }
        }
    }
}