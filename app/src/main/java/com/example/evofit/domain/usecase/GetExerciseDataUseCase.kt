package com.example.evofit.domain.usecase

import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.data.model.ExerciseModel

import com.example.evofit.domain.model.GoalSuggestion

class GetExerciseDataUseCase(private val dataSource: LocalExerciseDataSource) {
    fun getMuscleGroups(): List<MuscleGroupModel> = dataSource.getAllMuscleGroups()
    
    fun getExercisesByGroup(groupId: String): List<ExerciseModel> = 
        dataSource.getExercisesByMuscleGroup(groupId)

    fun getSuggestions(): List<GoalSuggestion> = dataSource.getSuggestions()
}
