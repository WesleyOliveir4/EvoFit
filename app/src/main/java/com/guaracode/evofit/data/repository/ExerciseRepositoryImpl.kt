package com.guaracode.evofit.data.repository

import com.guaracode.evofit.data.datasource.LocalExerciseDataSource
import com.guaracode.evofit.data.mapper.toDomain
import com.guaracode.evofit.domain.model.Exercise
import com.guaracode.evofit.domain.model.GoalSuggestion
import com.guaracode.evofit.domain.model.MuscleGroup
import com.guaracode.evofit.domain.repository.ExerciseRepository

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
