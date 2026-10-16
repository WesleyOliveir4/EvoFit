package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Exercise
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.repository.ExerciseRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class GetExercisesByGroupUseCaseTest {

    private val repository: ExerciseRepository = mockk(relaxed = true)
    private val useCase = GetExercisesByGroupUseCaseImpl(repository)

    @Test
    fun `should filter enabled exercises and sort by sortOrder`() {
        val ex1 = Exercise("1", "Supino Reto", "mg1", MeasurementUnit.WEIGHT, sortOrder = 2, isEnabled = true)
        val ex2 = Exercise("2", "Supino Inclinado", "mg1", MeasurementUnit.WEIGHT, sortOrder = 1, isEnabled = true)
        val ex3 = Exercise("3", "Flexão Desabilitada", "mg1", MeasurementUnit.REPS, sortOrder = 0, isEnabled = false)

        every { repository.getExercisesByGroup("mg1") } returns listOf(ex1, ex2, ex3)

        val result = useCase("mg1")

        assertEquals(2, result.size)
        assertEquals("2", result[0].id)
        assertEquals("1", result[1].id)
    }
}
