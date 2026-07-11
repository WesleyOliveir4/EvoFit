package com.example.evofit.data.repository

import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.mapper.toDomain
import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.repository.ExerciseRepository

class ExerciseRepositoryImpl(
    private val dataSource: LocalExerciseDataSource
) : ExerciseRepository {
    override fun getMuscleGroups(): List<MuscleGroup> {
        return dataSource.getAllMuscleGroups().map { it.toDomain() }
    }

    override fun getExercisesByGroup(groupId: String): List<Exercise> {
        return dataSource.getExercisesByMuscleGroup(groupId).map { it.toDomain() }
    }

    override fun getExercisesByIds(ids: List<String>): List<Exercise> {
        return dataSource.getExercisesByIds(ids).map { it.toDomain() }
    }

    override fun getSuggestions(): List<GoalSuggestion> {
        return dataSource.getSuggestions()
    }
}
