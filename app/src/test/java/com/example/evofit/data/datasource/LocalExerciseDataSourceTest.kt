package com.example.evofit.data.datasource

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalExerciseDataSourceTest {

    private val dataSource = LocalExerciseDataSource()

    @Test
    fun `getAllMuscleGroups should return non empty list`() {
        val groups = dataSource.getAllMuscleGroups()
        assertTrue(groups.isNotEmpty())
        assertEquals("1", groups.first().id)
        assertEquals("Costas", groups.first().name)
    }

    @Test
    fun `getAllExercises should return non empty list`() {
        val exercises = dataSource.getAllExercises()
        assertTrue(exercises.isNotEmpty())
    }

    @Test
    fun `getExercisesByMuscleGroup should filter exercises correctly`() {
        // Group "1" is BACK
        val backExercises = dataSource.getExercisesByMuscleGroup("1")
        assertTrue(backExercises.isNotEmpty())
        assertTrue(backExercises.all { it.muscleGroupId == "1" })
    }

    @Test
    fun `getExercisesByIds should return matching exercises`() {
        val ids = listOf("1", "11", "21")
        val result = dataSource.getExercisesByIds(ids)
        assertEquals(3, result.size)
        assertTrue(result.map { it.id }.containsAll(ids))
    }

    @Test
    fun `getSuggestions should return pre-configured suggestions`() {
        val suggestions = dataSource.getSuggestions()
        assertTrue(suggestions.isNotEmpty())
        assertEquals("1", suggestions.first().id)
    }

    @Test
    fun `getMuscleGroupWithExercises should return group and exercises when id exists`() {
        val result = dataSource.getMuscleGroupWithExercises("2") // Peito
        assertNotNull(result)
        assertEquals("Peito", result?.muscleGroup?.name)
        assertTrue(result?.exercises?.isNotEmpty() == true)
    }

    @Test
    fun `getMuscleGroupWithExercises should return null when group id does not exist`() {
        val result = dataSource.getMuscleGroupWithExercises("invalid_id")
        assertNull(result)
    }
}
