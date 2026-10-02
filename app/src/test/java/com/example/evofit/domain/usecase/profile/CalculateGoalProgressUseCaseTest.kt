package com.example.evofit.domain.usecase.profile

import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.model.WorkoutGroup
import com.example.evofit.domain.repository.OnboardingRepository
import com.example.evofit.domain.repository.WorkoutRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CalculateGoalProgressUseCaseTest {

    private val workoutRepository: WorkoutRepository = mockk(relaxed = true)
    private val onboardingRepository: OnboardingRepository = mockk(relaxed = true)
    private val useCase = CalculateGoalProgressUseCaseImpl(workoutRepository, onboardingRepository)

    @Test
    fun `should calculate strength goal progress from workout history`() = runBlocking {
        val goal = UserGoal.Strength("g1", "Supino", "100", MeasurementUnit.WEIGHT)
        val history = listOf(
            WorkoutDone(
                id = "wd1",
                userId = "u1",
                exercisesByGroup = listOf(
                    WorkoutGroup(
                        muscleGroupId = "mg1",
                        exercises = listOf(
                            WorkoutExercise(
                                exerciseId = "ex1",
                                sets = listOf(ExerciseSet(exerciseName = "Supino", load = 80.0))
                            )
                        )
                    )
                )
            )
        )

        every { workoutRepository.getWorkoutDoneHistory("u1") } returns flowOf(history)

        val progress = useCase(goal, "u1").first()

        assertNotNull(progress)
        assertEquals(80.0, progress.currentValue, 0.001)
        assertEquals(100.0, progress.targetValue, 0.001)
        assertEquals(80, progress.percentage)
        assertEquals("kg", progress.unit)
    }

    @Test
    fun `should calculate weight goal progress using user onboarding data`() = runBlocking {
        val goal = UserGoal.Weight("g2", "70.0")
        val userData = UserOnboardingData(weight = "80.0")

        every { workoutRepository.getWorkoutDoneHistory("u1") } returns flowOf(emptyList())
        every { onboardingRepository.getUserData() } returns flowOf(userData)

        val progress = useCase(goal, "u1").first()

        assertNotNull(progress)
        assertEquals(80.0, progress.currentValue, 0.001)
        assertEquals(70.0, progress.targetValue, 0.001)
        assertEquals("kg", progress.unit)
    }
}
