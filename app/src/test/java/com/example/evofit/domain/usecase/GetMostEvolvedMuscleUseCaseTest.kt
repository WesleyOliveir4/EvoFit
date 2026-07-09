package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.*
import com.example.evofit.domain.repository.WorkoutRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class GetMostEvolvedMuscleUseCaseTest {

    private val repository: WorkoutRepository = mockk()
    private val useCase = GetMostEvolvedMuscleUseCase(repository)

    @Test
    fun `should calculate muscle evolution using median of exercises evolution`() = runBlocking {
        // Arrange
        val userId = "user123"
        val period = "Tudo"
        val muscleGroup = MuscleGroup("mg1", "Chest", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

        // Exercise 1: Loads 10 to 20. 3rd lowest=12, 3rd highest=18. (18-12)/12 = 50%
        val loads1 = listOf(10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0)
        // Exercise 2: Loads 10 to 30. 3rd lowest=14, 3rd highest=26. (26-14)/14 = 85.7%
        val loads2 = listOf(10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0)
        
        // Median of 50% and 85.7% is (50 + 85.7) / 2 = 67.85%

        val history = mutableListOf<WorkoutDone>()
        loads1.forEachIndexed { index, load ->
            history.add(createWorkoutDone(userId, "ex1", "Bench Press", muscleGroup, load, dateFormat, index))
        }
        loads2.forEachIndexed { index, load ->
            history.add(createWorkoutDone(userId, "ex2", "Incline Press", muscleGroup, load, dateFormat, index + 20))
        }

        coEvery { repository.getWorkoutDoneHistory(userId) } returns history

        // Act
        val result = useCase(userId, period)

        // Assert
        assertEquals("Chest", result?.muscleGroupName)
        assertEquals(67.85, result?.evolutionPercentage ?: 0.0, 0.01)
    }

    private fun createWorkoutDone(
        userId: String,
        exerciseId: String,
        exerciseName: String,
        muscleGroup: MuscleGroup,
        load: Double,
        dateFormat: SimpleDateFormat,
        id: Int
    ) = WorkoutDone(
        id = id.toLong(),
        userId = userId,
        name = "Workout $id",
        muscleGroupId = muscleGroup.id,
        muscleGroup = muscleGroup,
        date = dateFormat.format(Date()),
        exercises = listOf(
            WorkoutExercise(
                exerciseId = exerciseId,
                sets = listOf(
                    ExerciseSet(
                        exerciseName = exerciseName,
                        setNumber = 1,
                        reps = 10,
                        load = load
                    )
                )
            )
        ),
        time = "45:00"
    )
}
