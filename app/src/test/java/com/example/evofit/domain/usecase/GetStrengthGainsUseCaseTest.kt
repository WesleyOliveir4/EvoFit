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

class GetStrengthGainsUseCaseTest {

    private val repository: WorkoutRepository = mockk()
    private val useCase = GetStrengthGainsUseCase(repository)

    @Test
    fun `when exercise has 11 records, gain should be calculated using 3rd lowest and 3rd highest`() = runBlocking {
        // Arrange
        val userId = "user123"
        val period = "Tudo"
        val exerciseId = "ex1"
        val exerciseName = "Bench Press"
        val muscleGroup = MuscleGroup("mg1", "Chest", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        
        // Loads: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110 (11 items)
        // Sorted: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110
        // 3rd lowest (index 2): 30
        // 3rd highest (index size-3 = 8): 90
        // Expected gain: 90 - 30 = 60
        
        val loads = listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        
        val history = loads.mapIndexed { index, load ->
            WorkoutDone(
                id = index.toLong(),
                userId = userId,
                name = "Workout $index",
                muscleGroupId = "mg1",
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

        coEvery { repository.getWorkoutDoneHistory(userId) } returns history

        // Act
        val result = useCase(userId, period)

        // Assert
        assertEquals(1, result?.size)
        assertEquals(exerciseName, result?.first()?.exerciseName)
        assertEquals(60.0, result?.first()?.gainKg ?: 0.0, 0.01)
    }

    @Test
    fun `when exercise has 10 records, should return null due to insufficient data`() = runBlocking {
        // Arrange
        val userId = "user123"
        val period = "Tudo"
        val exerciseId = "ex1"
        val muscleGroup = MuscleGroup("mg1", "Chest", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        
        val loads = listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0) // 10 items
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        
        val history = loads.mapIndexed { index, load ->
            WorkoutDone(
                id = index.toLong(),
                userId = userId,
                name = "Workout $index",
                muscleGroupId = "mg1",
                muscleGroup = muscleGroup,
                date = dateFormat.format(Date()),
                exercises = listOf(
                    WorkoutExercise(
                        exerciseId = exerciseId,
                        sets = listOf(
                            ExerciseSet(
                                exerciseName = "Bench Press",
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

        coEvery { repository.getWorkoutDoneHistory(userId) } returns history

        // Act
        val result = useCase(userId, period)

        // Assert
        assertNull(result)
    }
}
