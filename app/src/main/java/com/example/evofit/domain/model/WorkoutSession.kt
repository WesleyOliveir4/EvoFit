package com.example.evofit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSession(
    val workoutId: String,
    val startTime: Long,
    val completedSets: List<CompletedSet> = emptyList()
)

@Serializable
data class CompletedSet(
    val workoutExerciseId: String,
    val setNumber: Int
)

data class ActiveWorkoutSession(
    val workout: Workout,
    val startTime: Long,
    val completedSets: List<CompletedSet>
)
