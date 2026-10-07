package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseAnalyticsResult
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.model.WorkoutGroup
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ProcessExerciseAnalyticsUseCaseTest {

    private val weightUseCase: ProcessWeightAnalyticsUseCase = mockk(relaxed = true)
    private val distanceUseCase: ProcessDistanceAnalyticsUseCase = mockk(relaxed = true)
    private val timeUseCase: ProcessTimeAnalyticsUseCase = mockk(relaxed = true)
    private val repsUseCase: ProcessRepsAnalyticsUseCase = mockk(relaxed = true)

    private val useCase = ProcessExerciseAnalyticsUseCaseImpl(
        weightUseCase,
        distanceUseCase,
        timeUseCase,
        repsUseCase
    )

    @Test
    fun `when no history matching exerciseId, should return null`() {
        val result = useCase("ex1", emptyList())
        assertNull(result)
    }

    @Test
    fun `when unit is WEIGHT, should delegate to weightUseCase`() {
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
                                    ExerciseSet(exerciseName = "Supino", unit = MeasurementUnit.WEIGHT)
                                )
                            )
                        )
                    )
                )
            )
        )

        val analyticsResult = ExerciseAnalyticsResult(
            unit = MeasurementUnit.WEIGHT,
            maxRecord = "100kg",
            totalSets = "10",
            firstRecordDate = "01/01/2026",
            lastRecordDate = "01/01/2026",
            loadChartPoints = emptyList()
        )
        every { weightUseCase("ex1", any()) } returns analyticsResult

        val result = useCase("ex1", history)

        assertNotNull(result)
        assertEquals(MeasurementUnit.WEIGHT, result?.unit)
    }
}
