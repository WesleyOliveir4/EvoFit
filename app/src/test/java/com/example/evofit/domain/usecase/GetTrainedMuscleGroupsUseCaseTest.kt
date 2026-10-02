package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.MuscleGroupType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTrainedMuscleGroupsUseCaseTest {

    private val filterTrainedMuscleGroupsUseCase: FilterTrainedMuscleGroupsUseCase = mockk(relaxed = true)
    private val useCase = GetTrainedMuscleGroupsUseCaseImpl(filterTrainedMuscleGroupsUseCase)

    @Test
    fun `should delegate to filterTrainedMuscleGroupsUseCase`() {
        val mg = MuscleGroup("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH)
        every { filterTrainedMuscleGroupsUseCase(any(), any()) } returns listOf(mg)

        val result = useCase(emptyList(), listOf(mg))

        assertEquals(1, result.size)
        assertEquals("Peito", result.first().name)
        verify { filterTrainedMuscleGroupsUseCase(emptyList(), listOf(mg)) }
    }
}
