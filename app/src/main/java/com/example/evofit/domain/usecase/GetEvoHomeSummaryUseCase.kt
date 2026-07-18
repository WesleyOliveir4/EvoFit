package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.EvoHomeSummary
import com.example.evofit.domain.model.EvoPeriod

interface GetEvoHomeSummaryUseCase {
    suspend operator fun invoke(userId: String, period: EvoPeriod): EvoHomeSummary
}

class GetEvoHomeSummaryUseCaseImpl(
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val filterWorkoutHistoryByPeriodUseCase: FilterWorkoutHistoryByPeriodUseCase,
    private val getStrengthGainsUseCase: GetStrengthGainsUseCase,
    private val getMostEvolvedMuscleUseCase: GetMostEvolvedMuscleUseCase,
    private val getWorkoutsCountUseCase: GetWorkoutsCountUseCase,
    private val getLeastTrainedGroupUseCase: GetLeastTrainedGroupUseCase,
    private val getKmPerWeekUseCase: GetKmPerWeekUseCase,
    private val getAverageWorkoutTimeUseCase: GetAverageWorkoutTimeUseCase
) : GetEvoHomeSummaryUseCase {

    override suspend fun invoke(userId: String, period: EvoPeriod): EvoHomeSummary {
        val fullHistory = getWorkoutDoneHistoryUseCase(userId)
        val history = filterWorkoutHistoryByPeriodUseCase(fullHistory, period)

        return EvoHomeSummary(
            strengthGains = getStrengthGainsUseCase(history),
            mostEvolvedMuscle = getMostEvolvedMuscleUseCase(history),
            workoutsCount = getWorkoutsCountUseCase(history),
            leastTrainedGroup = getLeastTrainedGroupUseCase(history),
            kmPerWeek = getKmPerWeekUseCase(history),
            averageWorkoutTime = getAverageWorkoutTimeUseCase(history)
        )
    }
}
