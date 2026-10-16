package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Exercise
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.repository.ExerciseRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class GetExercisesByIdsUseCaseTest {

    private val repository: ExerciseRepository = mockk(relaxed = true)
    private val useCase = GetExercisesByIdsUseCaseImpl(repository)

    @Test
    fun `should delegate to exerciseRepository getExercisesByIds`() {
        val exercises = listOf(Exercise("1", "Supino", "mg1", MeasurementUnit.WEIGHT))
        every { repository.getExercisesByIds(listOf("1")) } returns exercises

        val result = useCase(listOf("1"))

        assertEquals(1, result.size)
        assertEquals("Supino", result.first().name)
        verify { repository.getExercisesByIds(listOf("1")) }
    }
}
