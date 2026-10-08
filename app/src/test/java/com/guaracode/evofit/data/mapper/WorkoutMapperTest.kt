package com.guaracode.evofit.data.mapper

import com.guaracode.evofit.data.local.entities.ActiveSessionEntity
import com.guaracode.evofit.data.local.entities.ActiveSessionSetEntity
import com.guaracode.evofit.data.local.entities.ExerciseSetEntity
import com.guaracode.evofit.data.local.entities.WorkoutDoneEntity
import com.guaracode.evofit.data.local.entities.WorkoutEntity
import com.guaracode.evofit.data.local.entities.WorkoutExerciseEntity
import com.guaracode.evofit.data.local.relations.FullWorkout
import com.guaracode.evofit.data.local.relations.WorkoutExerciseWithSets
import com.guaracode.evofit.domain.model.CompletedSet
import com.guaracode.evofit.domain.model.ExerciseCategory
import com.guaracode.evofit.domain.model.ExerciseSet
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.MuscleGroup
import com.guaracode.evofit.domain.model.MuscleGroupType
import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.model.WorkoutExercise
import com.guaracode.evofit.domain.model.WorkoutGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutMapperTest {

    @Test
    fun `FullWorkout toDomain should map full workout structure correctly`() {
        val workoutEntity = WorkoutEntity(
            workoutId = "w1",
            userId = "user1",
            name = "Treino A",
            date = "01/01/2026",
            orderIndex = 1
        )

        val exerciseEntity = WorkoutExerciseEntity(
            id = "we1",
            workoutId = "w1",
            exerciseId = "ex1",
            muscleGroupId = "mg1",
            orderIndex = 0,
            groupOrderIndex = 0,
            totalSets = 3
        )

        val setEntity = ExerciseSetEntity(
            id = "s1",
            workoutExerciseId = "we1",
            setNumber = 1,
            reps = 10,
            load = 50.0,
            unit = MeasurementUnit.WEIGHT
        )

        val fullWorkout = FullWorkout(
            workout = workoutEntity,
            exercises = listOf(
                WorkoutExerciseWithSets(
                    workoutExercise = exerciseEntity,
                    sets = listOf(setEntity)
                )
            )
        )

        val muscleGroup = MuscleGroup("mg1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        val resolver: (String) -> String = { id -> if (id == "ex1") "Supino" else "" }

        val domain = fullWorkout.toDomain(listOf(muscleGroup), resolver)

        assertEquals("w1", domain.id)
        assertEquals("user1", domain.userId)
        assertEquals("Treino A", domain.name)
        assertEquals("01/01/2026", domain.date)
        assertEquals(1, domain.orderIndex)
        assertEquals(1, domain.exercisesByGroup.size)

        val group = domain.exercisesByGroup.first()
        assertEquals("mg1", group.muscleGroupId)
        assertEquals("Peito", group.muscleGroup?.name)
        assertEquals(1, group.exercises.size)

        val exercise = group.exercises.first()
        assertEquals("we1", exercise.id)
        assertEquals("ex1", exercise.exerciseId)
        assertEquals(1, exercise.sets.size)
        assertEquals("Supino", exercise.sets.first().exerciseName)
    }

    @Test
    fun `ExerciseSetEntity toDomain should map all set properties`() {
        val setEntity = ExerciseSetEntity(
            id = "s10",
            workoutExerciseId = "we1",
            setNumber = 2,
            reps = 12,
            load = 60.0,
            unit = MeasurementUnit.WEIGHT,
            time = 30,
            distance = 5.0
        )

        val domain = setEntity.toDomain("Rosca Direta")

        assertEquals("s10", domain.id)
        assertEquals("Rosca Direta", domain.exerciseName)
        assertEquals("we1", domain.workoutExerciseId)
        assertEquals(2, domain.setNumber)
        assertEquals(12, domain.reps)
        assertEquals(60.0, domain.load, 0.001)
        assertEquals(MeasurementUnit.WEIGHT, domain.unit)
        assertEquals(30, domain.time)
        assertEquals(5.0, domain.distance!!, 0.001)
    }

    @Test
    fun `Workout toEntity should map correctly`() {
        val workout = Workout(
            id = "w1",
            userId = "user1",
            name = "Treino B",
            date = "02/01/2026",
            exercisesByGroup = emptyList(),
            orderIndex = 2
        )

        val entity = workout.toEntity()

        assertEquals("w1", entity.workoutId)
        assertEquals("user1", entity.userId)
        assertEquals("Treino B", entity.name)
        assertEquals("02/01/2026", entity.date)
        assertEquals(2, entity.orderIndex)
    }

    @Test
    fun `WorkoutExercise toEntity should map correctly`() {
        val workoutExercise = WorkoutExercise(
            id = "we1",
            exerciseId = "ex1",
            muscleGroupId = "mg1",
            sets = listOf(ExerciseSet(id = "s1", setNumber = 1, reps = 10, load = 20.0)),
            totalSets = 0,
            orderIndex = 1
        )

        val entity = workoutExercise.toEntity("w1", "mg1", 0)

        assertEquals("we1", entity.id)
        assertEquals("w1", entity.workoutId)
        assertEquals("ex1", entity.exerciseId)
        assertEquals("mg1", entity.muscleGroupId)
        assertEquals(1, entity.orderIndex)
        assertEquals(0, entity.groupOrderIndex)
        assertEquals(1, entity.totalSets) // totalSets fallback to sets.size when totalSets <= 0
    }

    @Test
    fun `ActiveSessionEntity and ActiveSessionSetEntity toDomain should map to WorkoutSession`() {
        val sessionEntity = ActiveSessionEntity(workoutId = "w10", startTime = 10000L)
        val setEntity = ActiveSessionSetEntity(workoutId = "w10", workoutExerciseId = "we1", setNumber = 1)

        val session = sessionEntity.toDomain(listOf(setEntity))

        assertEquals("w10", session.workoutId)
        assertEquals(10000L, session.startTime)
        assertEquals(1, session.completedSets.size)
        assertEquals("we1", session.completedSets.first().workoutExerciseId)
        assertEquals(1, session.completedSets.first().setNumber)
    }

    @Test
    fun `CompletedSet toEntity should map correctly`() {
        val completedSet = CompletedSet(workoutExerciseId = "we2", setNumber = 3)
        val entity = completedSet.toEntity("w20")

        assertEquals("w20", entity.workoutId)
        assertEquals("we2", entity.workoutExerciseId)
        assertEquals(3, entity.setNumber)
    }

    @Test
    fun `WorkoutDone toEntity and fixInconsistencies should function properly`() {
        val workoutDone = WorkoutDone(
            id = "wd1",
            userId = "user1",
            name = "Treino C",
            date = "03/01/2026",
            exercisesByGroup = listOf(
                WorkoutGroup(
                    muscleGroupId = "mg1",
                    exercises = listOf(
                        WorkoutExercise(
                            id = "we1",
                            exerciseId = "ex1",
                            muscleGroupId = "mg1",
                            totalSets = 0,
                            sets = listOf(ExerciseSet(id = "s1", setNumber = 1, reps = 10, load = 30.0))
                        )
                    )
                )
            ),
            time = "45:00",
            createdAt = 1000L
        )

        val fixed = workoutDone.fixInconsistencies()
        assertEquals(1, fixed.exercisesByGroup.first().exercises.first().totalSets)

        val entity = fixed.toEntity()
        assertEquals("wd1", entity.id)
        assertEquals("user1", entity.userId)
        assertEquals("Treino C", entity.name)
        assertEquals("03/01/2026", entity.date)
        assertEquals("45:00", entity.time)
        assertEquals(1000L, entity.createdAt)
    }

    @Test
    fun `WorkoutDoneEntity toDomain should resolve exercise names and muscle groups`() {
        val mg = MuscleGroup("mg1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        val entity = WorkoutDoneEntity(
            id = "wd2",
            userId = "user2",
            name = "Treino Concluído",
            date = "04/01/2026",
            exercisesByGroup = listOf(
                WorkoutGroup(
                    muscleGroupId = "mg1",
                    exercises = listOf(
                        WorkoutExercise(
                            id = "we1",
                            exerciseId = "ex1",
                            muscleGroupId = "mg1",
                            sets = listOf(
                                ExerciseSet(id = "s1", exerciseName = "", setNumber = 1, reps = 8, load = 40.0)
                            )
                        )
                    )
                )
            ),
            time = "30:00",
            createdAt = 2000L
        )

        val domain = entity.toDomain(listOf(mg), exerciseNameResolver = { id -> if (id == "ex1") "Supino Inclinado" else "" })

        assertEquals("wd2", domain.id)
        assertEquals("Peito", domain.exercisesByGroup.first().muscleGroup?.name)
        assertEquals("Supino Inclinado", domain.exercisesByGroup.first().exercises.first().sets.first().exerciseName)
    }
}
