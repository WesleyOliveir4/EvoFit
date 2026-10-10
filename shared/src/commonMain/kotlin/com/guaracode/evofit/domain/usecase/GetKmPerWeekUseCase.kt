package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WorkoutDone

interface GetKmPerWeekUseCase {
    operator fun invoke(history: List<WorkoutDone>): Double
}

class GetKmPerWeekUseCaseImpl : GetKmPerWeekUseCase {
    override fun invoke(history: List<WorkoutDone>): Double {
        if (history.isEmpty()) return 0.0
        
        var totalKm = 0.0
        for (workout in history) {
            for (group in workout.exercisesByGroup) {
                for (ex in group.exercises) {
                    for (set in ex.sets) {
                        if (set.unit == MeasurementUnit.DISTANCE && set.distance != null) {
                            totalKm += set.distance
                        }
                    }
                }
            }
        }
        
        return totalKm / maxOf(1, history.size / 4)
    }
}
