package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MuscleEvolution
import com.example.evofit.domain.repository.WorkoutRepository
import com.example.evofit.presentation.mapper.DateMapper
import java.util.Calendar
import java.util.Date

class GetMostEvolvedMuscleUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(userId: String, period: String): MuscleEvolution? {
        val history = repository.getWorkoutDoneHistory(userId)
        val startDate = getStartDateForPeriod(period)

        // 1. Filtrar período e categoria STRENGTH
        val filteredWorkouts = history.filter { workout ->
            val workoutDate = DateMapper.parseDate(workout.date)
            val isWithinPeriod = if (startDate == null) true else workoutDate?.after(startDate) == true
            isWithinPeriod && workout.muscleGroup?.category == ExerciseCategory.STRENGTH
        }

        // 2. Coletar cargas máximas por exercício e mapear exercício para seu grupo muscular
        val exercisesLoads = mutableMapOf<String, MutableList<Double>>()
        val exerciseToMuscleGroup = mutableMapOf<String, String>()

        filteredWorkouts.forEach { workout ->
            val muscleGroupName = workout.muscleGroup?.name ?: "Outros"
            workout.exercises.forEach { exercise ->
                val maxLoad = exercise.sets.maxOfOrNull { it.load } ?: 0.0
                if (maxLoad > 0) {
                    exercisesLoads.getOrPut(exercise.exerciseId) { mutableListOf() }.add(maxLoad)
                    exerciseToMuscleGroup[exercise.exerciseId] = muscleGroupName
                }
            }
        }

        // 3. Calcular porcentagem de evolução por exercício (apenas com > 10 resultados)
        val exercisePercentages = mutableMapOf<String, Double>()
        exercisesLoads.forEach { (exerciseId, loads) ->
            if (loads.size > 10) {
                val sortedLoads = loads.sorted()
                val thirdLowest = sortedLoads[2]
                val thirdHighest = sortedLoads[sortedLoads.size - 3]
                
                if (thirdLowest > 0) {
                    val percentage = (thirdHighest - thirdLowest) / thirdLowest * 100.0
                    exercisePercentages[exerciseId] = percentage
                }
            }
        }

        if (exercisePercentages.isEmpty()) return null

        // 4. Agrupar porcentagens por grupo muscular e calcular a mediana
        val muscleGroupMedians = exercisePercentages.entries
            .groupBy({ exerciseToMuscleGroup[it.key] ?: "Outros" }, { it.value })
            .mapValues { (_, percentages) ->
                calculateMedian(percentages)
            }

        // 5. Retornar o grupo com a maior mediana
        return muscleGroupMedians.maxByOrNull { it.value }?.let {
            MuscleEvolution(
                muscleGroupName = it.key,
                evolutionPercentage = it.value
            )
        }
    }

    private fun calculateMedian(list: List<Double>): Double {
        if (list.isEmpty()) return 0.0
        val sortedList = list.sorted()
        val size = sortedList.size
        return if (size % 2 == 0) {
            (sortedList[size / 2 - 1] + sortedList[size / 2]) / 2.0
        } else {
            sortedList[size / 2]
        }
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
