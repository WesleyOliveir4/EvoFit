package com.guaracode.evofit.domain.model

enum class EvoPeriod {
    LAST_30_DAYS,
    LAST_90_DAYS,
    LAST_180_DAYS,
    ALL_TIME
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
