package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.StrengthGain
import com.example.evofit.domain.model.WorkoutDone

interface GetStrengthGainsUseCase {
    operator fun invoke(history: List<WorkoutDone>): List<StrengthGain>?
}

class GetStrengthGainsUseCaseImpl : GetStrengthGainsUseCase {
    override fun invoke(history: List<WorkoutDone>): List<StrengthGain>? {
        val exercisesLoads = mutableMapOf<String, MutableList<Double>>()
        val exerciseNames = mutableMapOf<String, String>()

        history.forEach { workout ->
            workout.exercisesByGroup.forEach { group ->
                if (group.muscleGroup?.category == ExerciseCategory.STRENGTH) {
                    group.exercises.forEach { exercise ->
                        val maxLoad = exercise.sets.maxOfOrNull { it.load } ?: 0.0
                        if (maxLoad > 0) {
                            exercisesLoads.getOrPut(exercise.exerciseId) { mutableListOf() }.add(maxLoad)
                            exerciseNames.getOrPut(exercise.exerciseId) { 
                                exercise.sets.firstOrNull()?.exerciseName ?: "Exercício" 
                            }
                        }
                    }
                }
            }
        }

        val strengthGainsList = mutableListOf<StrengthGain>()

        exercisesLoads.forEach { (exerciseId, loads) ->
            if (loads.size > 10) {
                val sortedLoads = loads.sorted()
                val thirdLowest = sortedLoads[2]
                val thirdHighest = sortedLoads[sortedLoads.size - 3]
                
                val gainKg = thirdHighest - thirdLowest
                strengthGainsList.add(
                    StrengthGain(
                        exerciseName = exerciseNames[exerciseId] ?: "Exercício",
                        gainKg = gainKg
                    )
                )
            }
        }

        return if (strengthGainsList.isEmpty()) null 
               else strengthGainsList.sortedByDescending { it.gainKg }.take(3)
    }
}
