package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.ExerciseSet
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.model.WorkoutExercise
import com.guaracode.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProcessWeightAnalyticsUseCaseTest {

    private val useCase = ProcessWeightAnalyticsUseCaseImpl()

    @Test
    fun `should calculate max record and total sets for weight analytics`() {
        val history = listOf(
            WorkoutDone(
                id = "1",
                date = "01/01/2026",
                exercisesByGroup = listOf(
                    WorkoutGroup(
                        muscleGroupId = "mg1",
                        exercises = listOf(
                            WorkoutExercise(
                                exerciseId = "ex1",
                                sets = listOf(
                                    ExerciseSet(exerciseName = "Supino", setNumber = 1, reps = 10, load = 80.0)
                                )
                            )
                        )
                    )
                )
            )
        )

        val result = useCase("ex1", history)

        assertNotNull(result)
        assertEquals(MeasurementUnit.WEIGHT, result.unit)
        assertEquals("80kg", result.maxRecord)
        assertEquals("1", result.totalSets)
    }
}
