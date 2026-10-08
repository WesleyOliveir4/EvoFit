package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.ExerciseSet
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.model.WorkoutExercise
import com.guaracode.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class GetKmPerWeekUseCaseTest {

    private val useCase = GetKmPerWeekUseCaseImpl()

    @Test
    fun `when history is empty, should return 0`() {
        assertEquals(0.0, useCase(emptyList()), 0.001)
    }

    @Test
    fun `should calculate total distance correctly`() {
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
                                    ExerciseSet(exerciseName = "Esteira", setNumber = 1, unit = MeasurementUnit.DISTANCE, distance = 5.0)
                                )
                            )
                        )
                    )
                )
            )
        )

        val kmPerWeek = useCase(history)
        assertEquals(true, kmPerWeek > 0)
    }
}
