package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.StrengthGain
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import com.example.evofit.presentation.mapper.DateMapper
import java.util.Calendar
import java.util.Date

class GetStrengthGainsUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(userId: String, period: String): List<StrengthGain>? {
        val history = repository.getWorkoutDoneHistory(userId)
        val startDate = getStartDateForPeriod(period)

        // 1. Filtrar período e categoria STRENGTH
        val filteredWorkouts = history.filter { workout ->
            val workoutDate = DateMapper.parseDate(workout.date)
            val isWithinPeriod = if (startDate == null) true else workoutDate?.after(startDate) == true
            isWithinPeriod && workout.muscleGroup?.category == ExerciseCategory.STRENGTH
        }

        // 2. Agrupar sets por exercício e extrair o maior load de cada workout_exercise
        val exercisesLoads = mutableMapOf<String, MutableList<Double>>()
        val exerciseNames = mutableMapOf<String, String>()

        filteredWorkouts.forEach { workout ->
            workout.exercises.forEach { exercise ->
                val maxLoad = exercise.sets.maxOfOrNull { it.load } ?: 0.0
                if (maxLoad > 0) {
                    exercisesLoads.getOrPut(exercise.exerciseId) { mutableListOf() }.add(maxLoad)
                    // Pega o nome do exercício de qualquer set (todos devem ter o mesmo nome para o mesmo ID)
                    exercise.sets.firstOrNull()?.exerciseName?.let {
                        exerciseNames[exercise.exerciseId] = it
                    }
                }
            }
        }

        // 3. Processar ganhos
        val gains = exercisesLoads.mapNotNull { (exerciseId, loads) ->
            if (loads.size <= 10) return@mapNotNull null

            val sortedLoads = loads.sorted()
            val thirdLowest = sortedLoads[2]
            val thirdHighest = sortedLoads[sortedLoads.size - 3]
            val gain = thirdHighest - thirdLowest

            StrengthGain(
                exerciseName = exerciseNames[exerciseId] ?: "Exercício",
                gainKg = gain
            )
        }

        if (gains.isEmpty()) return null

        // 4. Retornar os 3 com maior ganho
        return gains.sortedByDescending { it.gainKg }.take(3)
    }

    private fun getStartDateForPeriod(period: String): Date? {
        val calendar = Calendar.getInstance()
        return when (period) {
            "1 mês" -> {
                calendar.add(Calendar.MONTH, -1)
                calendar.time
            }
            "3 meses" -> {
                calendar.add(Calendar.MONTH, -3)
                calendar.time
            }
            "6 meses" -> {
                calendar.add(Calendar.MONTH, -6)
                calendar.time
            }
            else -> null
        }
    }
}
