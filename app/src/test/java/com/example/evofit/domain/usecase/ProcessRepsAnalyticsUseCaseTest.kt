package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProcessRepsAnalyticsUseCaseTest {

    private val useCase = ProcessRepsAnalyticsUseCaseImpl()

    @Test
    fun `should process reps analytics`() {
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
                                        exerciseName = "Flexão",
                                        setNumber = 1,
                                        unit = MeasurementUnit.REPS,
                                        reps = 25
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
        assertEquals(MeasurementUnit.REPS, result.unit)
        assertEquals("25 reps", result.maxRecord)
    }
}
