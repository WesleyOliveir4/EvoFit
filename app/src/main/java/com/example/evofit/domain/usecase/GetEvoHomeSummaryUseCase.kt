package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.EvoHomeSummary
import com.example.evofit.domain.model.EvoPeriod

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetEvoHomeSummaryUseCase {
    operator fun invoke(userId: String, period: EvoPeriod): Flow<EvoHomeSummary>
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

    override fun invoke(userId: String, period: EvoPeriod): Flow<EvoHomeSummary> {
        return getWorkoutDoneHistoryUseCase(userId, 500).map { fullHistory ->
            val history = filterWorkoutHistoryByPeriodUseCase(fullHistory, period)

            EvoHomeSummary(
                strengthGains = getStrengthGainsUseCase(history),
                mostEvolvedMuscle = getMostEvolvedMuscleUseCase(history),
                workoutsCount = getWorkoutsCountUseCase(history),
                leastTrainedGroup = getLeastTrainedGroupUseCase(history),
                kmPerWeek = getKmPerWeekUseCase(history),
                averageWorkoutTime = getAverageWorkoutTimeUseCase(history)
            )
        }
    }
}
