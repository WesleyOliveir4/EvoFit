package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WorkoutDone

interface GetAverageWorkoutTimeUseCase {
    operator fun invoke(history: List<WorkoutDone>): Int
}

class GetAverageWorkoutTimeUseCaseImpl : GetAverageWorkoutTimeUseCase {
    override fun invoke(history: List<WorkoutDone>): Int {
        if (history.isEmpty()) return 0
        
        var totalMinutes = 0
        var count = 0

        for (workout in history) {
            val parts = workout.time.split(":")
            if (parts.size >= 2) {
                val hours = parts[0].toIntOrNull() ?: 0
                val minutes = parts[1].toIntOrNull() ?: 0
                totalMinutes += (hours * 60) + minutes
                count++
            }
        }

        return if (count > 0) totalMinutes / count else 0
    }
}
