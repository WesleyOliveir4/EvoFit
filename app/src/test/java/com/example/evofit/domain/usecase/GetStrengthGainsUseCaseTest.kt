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

class GetStrengthGainsUseCaseTest {

    private val useCase = GetStrengthGainsUseCaseImpl()

    @Test
    fun `when exercise has 10 or fewer records, strength gains should be null`() {
        val mg = MuscleGroup("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        val history = (1..5).map { i ->
            WorkoutDone(
                id = "$i",
                exercisesByGroup = listOf(
                    WorkoutGroup(
                        muscleGroupId = "1",
                        muscleGroup = mg,
                        exercises = listOf(
                            WorkoutExercise(
                                exerciseId = "ex1",
                                sets = listOf(ExerciseSet(exerciseName = "Supino", setNumber = 1, load = i * 10.0))
                            )
                        )
                    )
                )
            )
        }

        val result = useCase(history)

        assertNull(result)
    }

    @Test
    fun `when exercise has more than 10 records, should calculate strength gain`() {
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
        assertEquals(1, result?.size)
        assertEquals("Supino", result?.first()?.exerciseName)
        assertEquals(60.0, result?.first()?.gainKg ?: 0.0, 0.01)
    }
}
