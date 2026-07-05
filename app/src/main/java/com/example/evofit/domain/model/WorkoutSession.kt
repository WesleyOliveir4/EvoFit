package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSession(
    val workoutId: Long,
    val startTime: Long,
    val completedSets: List<CompletedSet> = emptyList()
)

@Serializable
data class CompletedSet(
    val workoutExerciseId: Long,
    val setNumber: Int
)

data class ActiveWorkoutSession(
    val workout: Workout,
    val startTime: Long,
    val completedSets: List<CompletedSet>
)
