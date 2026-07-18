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

class GetEvoHomeSummaryUseCaseTest {

    private val repository: WorkoutRepository = mockk()
    private val getWorkoutDoneHistoryUseCase = GetWorkoutDoneHistoryUseCaseImpl(repository)
    private val filterWorkoutHistoryByPeriodUseCase: FilterWorkoutHistoryByPeriodUseCase =
        FilterWorkoutHistoryByPeriodUseCaseImpl()
    private val useCase = GetEvoHomeSummaryUseCaseImpl(
        getWorkoutDoneHistoryUseCase,
        filterWorkoutHistoryByPeriodUseCase,
        GetStrengthGainsUseCaseImpl(),
        GetMostEvolvedMuscleUseCaseImpl(),
        GetWorkoutsCountUseCaseImpl(),
        GetLeastTrainedGroupUseCaseImpl(),
        GetKmPerWeekUseCaseImpl(),
        GetAverageWorkoutTimeUseCaseImpl()
    )

    @Test
    fun `when data is sufficient, should calculate all metrics correctly`() = runBlocking {
        // Arrange
        val userId = "user123"
        val period = EvoPeriod.ALL_TIME
        val muscleGroup = MuscleGroup("mg1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        
        // Exercise 1: 11 records, 3rd lowest=30, 3rd highest=90, gain=60, evolution=(90-30)/30=200%
        val loads1 = listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0)
        val history = loads1.mapIndexed { index, load ->
            createWorkoutDone(userId, index.toLong(), "Supino", "ex1", load, muscleGroup)
        }

        coEvery { repository.getWorkoutDoneHistory(userId) } returns history

        // Act
        val result = useCase(userId, period)

        // Assert
        assertEquals(11, result.workoutsCount)
        assertEquals(1, result.strengthGains?.size)
        assertEquals(60.0, result.strengthGains?.first()?.gainKg ?: 0.0, 0.01)
        assertEquals("Peito", result.mostEvolvedMuscle?.muscleGroupName)
        assertEquals(200.0, result.mostEvolvedMuscle?.evolutionPercentage ?: 0.0, 0.01)
    }

    @Test
    fun `when insufficient data, metrics should be null`() = runBlocking {
        // Arrange
        val userId = "user123"
        val period = EvoPeriod.ALL_TIME
        val muscleGroup = MuscleGroup("mg1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        
        // Only 5 records
        val history = (1..5).map { index ->
            createWorkoutDone(userId, index.toLong(), "Supino", "ex1", index * 10.0, muscleGroup)
        }

        coEvery { repository.getWorkoutDoneHistory(userId) } returns history

        // Act
        val result = useCase(userId, period)

        // Assert
        assertEquals(5, result.workoutsCount)
        assertNull(result.strengthGains)
        assertNull(result.mostEvolvedMuscle)
    }

    private fun createWorkoutDone(
        userId: String, 
        id: Long, 
        exName: String, 
        exId: String, 
        load: Double,
        muscleGroup: MuscleGroup
    ): WorkoutDone {
        return WorkoutDone(
            id = id,
            userId = userId,
            name = "Treino $id",
            muscleGroupId = muscleGroup.id,
            muscleGroup = muscleGroup,
            date = "01/01/2024",
            exercises = listOf(
                WorkoutExercise(
                    exerciseId = exId,
                    sets = listOf(
                        ExerciseSet(exerciseName = exName, setNumber = 1, reps = 10, load = load)
                    )
                )
            ),
            time = "45:00"
        )
    }
}
