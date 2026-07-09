package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.EvoHomeSummary
import com.example.evofit.domain.model.EvoPeriod
import com.example.evofit.domain.repository.WorkoutRepository

interface GetEvoHomeSummaryUseCase {
    suspend operator fun invoke(userId: String, period: EvoPeriod): EvoHomeSummary
}

class GetEvoHomeSummaryUseCaseImpl(
    private val repository: WorkoutRepository,
    private val getStrengthGainsUseCase: GetStrengthGainsUseCase,
    private val getMostEvolvedMuscleUseCase: GetMostEvolvedMuscleUseCase,
    private val getWorkoutsCountUseCase: GetWorkoutsCountUseCase
) : GetEvoHomeSummaryUseCase {

    override suspend fun invoke(userId: String, period: EvoPeriod): EvoHomeSummary {
        val history = repository.getWorkoutDoneHistory(userId, period)
        
        return EvoHomeSummary(
            strengthGains = getStrengthGainsUseCase(history),
            mostEvolvedMuscle = getMostEvolvedMuscleUseCase(history),
            workoutsCount = getWorkoutsCountUseCase(history)
        )
    }
}
