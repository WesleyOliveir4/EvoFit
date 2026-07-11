package com.example.evofit.domain.model

import com.example.evofit.R

enum class EvoPeriod(val displayNameRes: Int) {
    LAST_7_DAYS(R.string.evo_period_7_days),
    LAST_30_DAYS(R.string.evo_period_30_days),
    LAST_90_DAYS(R.string.evo_period_90_days),
    LAST_180_DAYS(R.string.evo_period_180_days),
    ALL_TIME(R.string.evo_period_all_time)
}

data class StrengthGain(
    val exerciseName: String,
    val gainKg: Double
)

data class MuscleEvolution(
    val muscleGroupName: String,
    val evolutionPercentage: Double
)

data class EvoHomeSummary(
    val strengthGains: List<StrengthGain>?,
    val mostEvolvedMuscle: MuscleEvolution?,
    val workoutsCount: Int,
    val leastTrainedGroup: Pair<String, Int>?,
    val kmPerWeek: Double,
    val averageWorkoutTime: Int
)
