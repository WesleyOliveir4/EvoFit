package com.guaracode.evofit.domain.repository

import com.guaracode.evofit.domain.model.Exercise
import com.guaracode.evofit.domain.model.GoalSuggestion
import com.guaracode.evofit.domain.model.MuscleGroup

interface ExerciseRepository {
    fun getMuscleGroups(): List<MuscleGroup>
    fun getExercisesByGroup(groupId: String): List<Exercise>
    fun getExercisesByIds(ids: List<String>): List<Exercise>
    fun getSuggestions(): List<GoalSuggestion>
}
