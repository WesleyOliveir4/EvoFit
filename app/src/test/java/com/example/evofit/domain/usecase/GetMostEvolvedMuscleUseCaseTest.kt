package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GetMostEvolvedMuscleUseCaseTest {

    private val useCase = GetMostEvolvedMuscleUseCaseImpl()

    @Test
    fun `when data is insufficient, should return null`() {
        val result = useCase(emptyList())
        assertNull(result)
    }

    @Test
    fun `when history has more than 10 records per exercise, should calculate evolution percentage`() {
        val mg = MuscleGroup("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        val loads = listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0)
        val history = loads.mapIndexed { index, load ->
            WorkoutDone(
                id = "$index",
                exercisesByGroup = listOf(
                    WorkoutGroup(
                        muscleGroupId = "1",
                        muscleGroup = mg,
                        exercises = listOf(
                            WorkoutExercise(
                                exerciseId = "ex1",
                                sets = listOf(ExerciseSet(exerciseName = "Supino", setNumber = 1, load = load))
                            )
                        )
                    )
                )
            )
        }

        val result = useCase(history)

        assertNotNull(result)
        assertEquals("Peito", result?.muscleGroupName)
        assertEquals(200.0, result?.evolutionPercentage ?: 0.0, 0.01)
    }
}
