package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import java.time.Duration
import java.time.LocalTime

interface GetAverageWorkoutTimeUseCase {
    operator fun invoke(history: List<WorkoutDone>): Int
}

class GetAverageWorkoutTimeUseCaseImpl : GetAverageWorkoutTimeUseCase {
    override fun invoke(history: List<WorkoutDone>): Int {
        if (history.isEmpty()) return 0

        val totalMinutes = history.sumOf { workout ->
            try {
                val timeParts = workout.time.split(":")
                if (timeParts.size == 3) {
                    val hours = timeParts[0].toInt()
                    val minutes = timeParts[1].toInt()
                    val seconds = timeParts[2].toInt()
                    hours * 60 + minutes + (if (seconds > 0) 1 else 0)
                } else {
                    0
                }
            } catch (e: Exception) {
                0
            }
        }

        return (totalMinutes / history.size)
    }
}
