package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.ExerciseSet
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.model.WorkoutExercise
import com.guaracode.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProcessTimeAnalyticsUseCaseTest {

    private val useCase = ProcessTimeAnalyticsUseCaseImpl()

    @Test
    fun `should process time analytics`() {
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
                                    ExerciseSet(
                                        exerciseName = "Prancha",
                                        setNumber = 1,
                                        unit = MeasurementUnit.TIME,
                                        time = 120
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        val result = useCase("ex1", history)

        assertNotNull(result)
        assertEquals(MeasurementUnit.TIME, result.unit)
        assertEquals("1", result.totalSets)
    }
}
