package com.example.evofit.domain.model

enum class EvoPeriod(val displayName: String) {
    LAST_7_DAYS("7 dias"),
    LAST_30_DAYS("1 mês"),
    LAST_90_DAYS("3 meses"),
    LAST_180_DAYS("6 meses"),
    ALL_TIME("Tudo")
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
    val workoutsCount: Int
)
