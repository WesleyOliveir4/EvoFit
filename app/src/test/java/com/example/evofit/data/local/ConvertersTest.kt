package com.example.evofit.data.local

import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun `WorkoutDoneList serialization and deserialization should preserve data`() {
        val originalList = listOf(
            WorkoutDone(
                id = "wd1",
                userId = "u1",
                name = "Treino 1",
                date = "01/01/2026",
                exercisesByGroup = listOf(
                    WorkoutGroup(
                        muscleGroupId = "mg1",
                        exercises = listOf(
                            WorkoutExercise(
                                id = "we1",
                                exerciseId = "ex1",
                                muscleGroupId = "mg1",
                                sets = listOf(ExerciseSet(id = "s1", setNumber = 1, reps = 10, load = 50.0))
                            )
                        )
                    )
                ),
                time = "40:00",
                createdAt = 1000L
            )
        )

        val serialized = converters.fromWorkoutDoneList(originalList)
        val deserialized = converters.toWorkoutDoneList(serialized)

        assertEquals(originalList.size, deserialized.size)
        assertEquals(originalList.first().id, deserialized.first().id)
        assertEquals(originalList.first().name, deserialized.first().name)
        assertEquals(originalList.first().exercisesByGroup.size, deserialized.first().exercisesByGroup.size)
    }

    @Test
    fun `WorkoutGroupList serialization and deserialization should preserve data`() {
        val originalGroups = listOf(
            WorkoutGroup(
                muscleGroupId = "mg2",
                orderIndex = 1,
                exercises = listOf(
                    WorkoutExercise(
                        id = "we2",
                        exerciseId = "ex2",
                        muscleGroupId = "mg2",
                        sets = listOf(ExerciseSet(id = "s2", setNumber = 1, reps = 12, load = 30.0))
                    )
                )
            )
        )

        val serialized = converters.fromWorkoutGroupList(originalGroups)
        val deserialized = converters.toWorkoutGroupList(serialized)

        assertEquals(originalGroups.size, deserialized.size)
        assertEquals(originalGroups.first().muscleGroupId, deserialized.first().muscleGroupId)
        assertEquals(originalGroups.first().exercises.size, deserialized.first().exercises.size)
    }
}
