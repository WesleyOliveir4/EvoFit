package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseAnalyticsResult
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone

interface ProcessExerciseAnalyticsUseCase {
    operator fun invoke(exerciseId: String, history: List<WorkoutDone>): ExerciseAnalyticsResult?
}

class ProcessExerciseAnalyticsUseCaseImpl(
    private val weightUseCase: ProcessWeightAnalyticsUseCase,
    private val distanceUseCase: ProcessDistanceAnalyticsUseCase,
    private val timeUseCase: ProcessTimeAnalyticsUseCase,
    private val repsUseCase: ProcessRepsAnalyticsUseCase
) : ProcessExerciseAnalyticsUseCase {
    override fun invoke(exerciseId: String, history: List<WorkoutDone>): ExerciseAnalyticsResult? {
        val filteredWorkouts = history.filter { workout ->
            workout.exercises.any { it.exerciseId == exerciseId }
        }.sortedBy { workout ->
            try {
                if (workout.date.contains("/")) {
                    val parts = workout.date.split("/")
                    "${parts[2]}${parts[1]}${parts[0]}"
                } else {
                    workout.date
                }
            } catch (e: Exception) {
                workout.date
            }
        }

        if (filteredWorkouts.isEmpty()) return null

        val firstExerciseOccurrence = filteredWorkouts.first().exercises.first { it.exerciseId == exerciseId }
        val unit = firstExerciseOccurrence.sets.firstOrNull()?.unit ?: MeasurementUnit.WEIGHT

        return when (unit) {
            MeasurementUnit.WEIGHT -> weightUseCase(exerciseId, filteredWorkouts)
            MeasurementUnit.DISTANCE -> distanceUseCase(exerciseId, filteredWorkouts)
            MeasurementUnit.TIME -> timeUseCase(exerciseId, filteredWorkouts)
            MeasurementUnit.REPS -> repsUseCase(exerciseId, filteredWorkouts)
        }
    }
}
