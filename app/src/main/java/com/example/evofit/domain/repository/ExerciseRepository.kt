package com.example.evofit.domain.repository

import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.MuscleGroup

interface ExerciseRepository {
    fun getMuscleGroups(): List<MuscleGroup>
    fun getExercisesByGroup(groupId: String): List<Exercise>
    fun getExercisesByIds(ids: List<String>): List<Exercise>
    fun getSuggestions(): List<GoalSuggestion>
}
