package com.example.evofit.domain.model

data class StrengthGain(
    val exerciseName: String,
    val gainKg: Double
)

data class MuscleEvolution(
    val muscleGroupName: String,
    val evolutionPercentage: Double
)
