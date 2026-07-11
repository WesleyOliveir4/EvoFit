package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.repository.ExerciseRepository

class GetExerciseDataUseCase(private val repository: ExerciseRepository) {
    fun getMuscleGroups(): List<MuscleGroup> = repository.getMuscleGroups()
    
    fun getExercisesByGroup(groupId: String): List<Exercise> = 
        repository.getExercisesByGroup(groupId)

    fun getExercisesByIds(ids: List<String>): List<Exercise> =
        repository.getExercisesByIds(ids)

    fun getSuggestions(): List<GoalSuggestion> = repository.getSuggestions()
}
