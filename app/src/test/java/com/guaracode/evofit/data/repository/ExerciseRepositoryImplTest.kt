package com.guaracode.evofit.data.repository

import com.guaracode.evofit.data.datasource.LocalExerciseDataSource
import com.guaracode.evofit.data.model.ExerciseModel
import com.guaracode.evofit.data.model.MuscleGroupModel
import com.guaracode.evofit.data.model.MuscleGroupType
import com.guaracode.evofit.domain.model.ExerciseCategory
import com.guaracode.evofit.domain.model.GoalSuggestion
import com.guaracode.evofit.domain.model.MeasurementUnit
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseRepositoryImplTest {

    private val dataSource: LocalExerciseDataSource = mockk(relaxed = true)
    private val repository = ExerciseRepositoryImpl(dataSource)

    @Test
    fun `getMuscleGroups should map models from dataSource to domain`() {
        val model = MuscleGroupModel("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        every { dataSource.getAllMuscleGroups() } returns listOf(model)

        val result = repository.getMuscleGroups()

        assertEquals(1, result.size)
        assertEquals("1", result.first().id)
        assertEquals("Peito", result.first().name)
        verify { dataSource.getAllMuscleGroups() }
    }

    @Test
    fun `getExercisesByGroup should filter and map models from dataSource`() {
        val model = ExerciseModel("10", "Supino", "1", MeasurementUnit.WEIGHT)
        every { dataSource.getExercisesByMuscleGroup("1") } returns listOf(model)

        val result = repository.getExercisesByGroup("1")

        assertEquals(1, result.size)
        assertEquals("10", result.first().id)
        assertEquals("Supino", result.first().name)
        verify { dataSource.getExercisesByMuscleGroup("1") }
    }

    @Test
    fun `getExercisesByIds should filter and map models from dataSource`() {
        val model = ExerciseModel("10", "Supino", "1", MeasurementUnit.WEIGHT)
        every { dataSource.getExercisesByIds(listOf("10")) } returns listOf(model)

        val result = repository.getExercisesByIds(listOf("10"))

        assertEquals(1, result.size)
        assertEquals("10", result.first().id)
        verify { dataSource.getExercisesByIds(listOf("10")) }
    }

    @Test
    fun `getSuggestions should delegate directly to dataSource`() {
        val suggestion = GoalSuggestion("s1", "Ganhar Peito", ExerciseCategory.STRENGTH)
        every { dataSource.getSuggestions() } returns listOf(suggestion)

        val result = repository.getSuggestions()

        assertEquals(1, result.size)
        assertEquals("s1", result.first().id)
        verify { dataSource.getSuggestions() }
    }
}
