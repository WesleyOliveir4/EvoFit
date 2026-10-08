package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.Exercise
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.model.WorkoutExercise
import com.guaracode.evofit.domain.model.WorkoutGroup
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class GetExercisesWithRecordCountUseCaseTest {

    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase = mockk(relaxed = true)
    private val useCase = GetExercisesWithRecordCountUseCaseImpl(getExercisesByGroupUseCase)

    @Test
    fun `should return exercises with record count greater than zero`() {
        val ex1 = Exercise("ex1", "Supino", "mg1", MeasurementUnit.WEIGHT)
        val ex2 = Exercise("ex2", "Peck Deck", "mg1", MeasurementUnit.WEIGHT)

        every { getExercisesByGroupUseCase("mg1") } returns listOf(ex1, ex2)

        val history = listOf(
            WorkoutDone(
                id = "wd1",
                userId = "u1",
                exercisesByGroup = listOf(
                    WorkoutGroup(
                        muscleGroupId = "mg1",
                        exercises = listOf(
                            WorkoutExercise(exerciseId = "ex1"),
                            WorkoutExercise(exerciseId = "ex1")
                        )
                    )
                )
            )
        )

        val result = useCase("mg1", history)

        assertEquals(1, result.size)
        assertEquals("ex1", result.first().exercise.id)
        assertEquals(2, result.first().recordsCount)
    }
}
