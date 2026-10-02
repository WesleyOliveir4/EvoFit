package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetLeastTrainedGroupUseCaseTest {

    private val useCase = GetLeastTrainedGroupUseCaseImpl()

    @Test
    fun `when history is empty, should return null`() {
        val result = useCase(emptyList())
        assertNull(result)
    }

    @Test
    fun `should calculate group with minimum workout frequency`() {
        val mg1 = MuscleGroup("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        val mg2 = MuscleGroup("2", "Pernas", MuscleGroupType.LEGS, ExerciseCategory.STRENGTH)

        val history = listOf(
            WorkoutDone(id = "wd1", userId = "u1", exercisesByGroup = listOf(WorkoutGroup(muscleGroupId = "1", muscleGroup = mg1))),
            WorkoutDone(id = "wd2", userId = "u1", exercisesByGroup = listOf(WorkoutGroup(muscleGroupId = "1", muscleGroup = mg1))),
            WorkoutDone(id = "wd3", userId = "u1", exercisesByGroup = listOf(WorkoutGroup(muscleGroupId = "2", muscleGroup = mg2)))
        )

        val result = useCase(history)

        assertEquals("Pernas", result?.first)
        assertEquals(1, result?.second)
    }
}
