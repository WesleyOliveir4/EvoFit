package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.ExerciseCategory
import com.guaracode.evofit.domain.model.MuscleGroup
import com.guaracode.evofit.domain.model.MuscleGroupType
import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterTrainedMuscleGroupsUseCaseTest {

    private val useCase = FilterTrainedMuscleGroupsUseCaseImpl()

    @Test
    fun `should filter allGroups returning only groups that appear in history`() {
        val mg1 = MuscleGroup("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        val mg2 = MuscleGroup("2", "Costas", MuscleGroupType.BACK, ExerciseCategory.STRENGTH)
        val mg3 = MuscleGroup("3", "Pernas", MuscleGroupType.LEGS, ExerciseCategory.STRENGTH)

        val history = listOf(
            WorkoutDone(
                id = "wd1",
                userId = "u1",
                exercisesByGroup = listOf(
                    WorkoutGroup(muscleGroupId = "1", exercises = emptyList())
                )
            )
        )

        val trainedGroups = useCase(history, listOf(mg1, mg2, mg3))

        assertEquals(1, trainedGroups.size)
        assertEquals("Peito", trainedGroups.first().name)
    }
}
