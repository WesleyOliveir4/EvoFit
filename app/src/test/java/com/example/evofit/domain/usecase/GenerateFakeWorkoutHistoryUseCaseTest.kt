package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.repository.OnboardingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GenerateFakeWorkoutHistoryUseCaseTest {

    private val getUserIdUseCase: GetUserIdUseCase = mockk(relaxed = true)
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase = mockk(relaxed = true)
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase = mockk(relaxed = true)
    private val saveWorkoutDoneUseCase: SaveWorkoutDoneUseCase = mockk(relaxed = true)
    private val onboardingRepository: OnboardingRepository = mockk(relaxed = true)

    private val useCase = GenerateFakeWorkoutHistoryUseCaseImpl(
        getUserIdUseCase,
        getMuscleGroupsUseCase,
        getExercisesByGroupUseCase,
        saveWorkoutDoneUseCase,
        onboardingRepository
    )

    @Test
    fun `when userId is null, should do nothing`() = runBlocking {
        coEvery { getUserIdUseCase() } returns flowOf(null)

        useCase()

        coVerify(exactly = 0) { saveWorkoutDoneUseCase(any(), any()) }
    }

    @Test
    fun `when sufficient muscle groups and exercises exist, should generate and save workouts`() = runBlocking {
        coEvery { getUserIdUseCase() } returns flowOf("u123")

        val mg1 = MuscleGroup("mg1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        val mg2 = MuscleGroup("mg2", "Costas", MuscleGroupType.BACK, ExerciseCategory.STRENGTH)
        val mg3 = MuscleGroup("mg3", "Pernas", MuscleGroupType.LEGS, ExerciseCategory.STRENGTH)

        val ex1 = Exercise("ex1", "Supino", "mg1", MeasurementUnit.WEIGHT)
        val ex2 = Exercise("ex2", "Remada", "mg2", MeasurementUnit.WEIGHT)
        val ex3 = Exercise("ex3", "Agachamento", "mg3", MeasurementUnit.WEIGHT)

        coEvery { getMuscleGroupsUseCase() } returns listOf(mg1, mg2, mg3)
        coEvery { getExercisesByGroupUseCase("mg1") } returns listOf(ex1)
        coEvery { getExercisesByGroupUseCase("mg2") } returns listOf(ex2)
        coEvery { getExercisesByGroupUseCase("mg3") } returns listOf(ex3)

        useCase()

        coVerify(atLeast = 1) { saveWorkoutDoneUseCase("u123", any()) }
    }
}
