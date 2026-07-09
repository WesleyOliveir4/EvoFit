package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MuscleEvolution
import com.example.evofit.domain.model.WorkoutDone

interface GetMostEvolvedMuscleUseCase {
    operator fun invoke(history: List<WorkoutDone>): MuscleEvolution?
}

class GetMostEvolvedMuscleUseCaseImpl : GetMostEvolvedMuscleUseCase {
    override fun invoke(history: List<WorkoutDone>): MuscleEvolution? {
        val strengthHistory = history.filter { 
            it.muscleGroup?.category == ExerciseCategory.STRENGTH 
        }

        val exercisesLoads = mutableMapOf<String, MutableList<Double>>()
        val exerciseToMuscleGroup = mutableMapOf<String, String>()

        strengthHistory.forEach { workout ->
            val muscleGroupName = workout.muscleGroup?.name ?: "Outros"
            workout.exercises.forEach { exercise ->
                val maxLoad = exercise.sets.maxOfOrNull { it.load } ?: 0.0
                if (maxLoad > 0) {
                    exercisesLoads.getOrPut(exercise.exerciseId) { mutableListOf() }.add(maxLoad)
                    exerciseToMuscleGroup[exercise.exerciseId] = muscleGroupName
                }
            }
        }

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

        return if (exercisePercentages.isEmpty()) null
        else {
            val muscleGroupMedians = exercisePercentages.entries
                .groupBy({ exerciseToMuscleGroup[it.key] ?: "Outros" }, { it.value })
                .mapValues { (_, percentages) ->
                    calculateMedian(percentages)
                }
            
            muscleGroupMedians.maxByOrNull { it.value }?.let {
                MuscleEvolution(
                    muscleGroupName = it.key,
                    evolutionPercentage = it.value
                )
            }
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
}
