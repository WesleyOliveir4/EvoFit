package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProcessDistanceAnalyticsUseCaseTest {

    private val useCase = ProcessDistanceAnalyticsUseCaseImpl()

    @Test
    fun `should calculate distance and speed analytics`() {
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
                                        exerciseName = "Esteira",
                                        setNumber = 1,
                                        unit = MeasurementUnit.DISTANCE,
                                        distance = 5.0,
                                        time = 30
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
        assertEquals(MeasurementUnit.DISTANCE, result.unit)
        assertEquals("5.00km", result.maxRecord)
        assertEquals("10.0 km/h", result.secondaryRecord) // 5km em 30min -> 10km/h
    }
}
