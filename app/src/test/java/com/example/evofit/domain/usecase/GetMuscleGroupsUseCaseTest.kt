package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.repository.ExerciseRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMuscleGroupsUseCaseTest {

    private val repository: ExerciseRepository = mockk(relaxed = true)
    private val useCase = GetMuscleGroupsUseCaseImpl(repository)

    @Test
    fun `should delegate to exerciseRepository getMuscleGroups`() {
        val groups = listOf(MuscleGroup("1", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH))
        every { repository.getMuscleGroups() } returns groups

        val result = useCase()

        assertEquals(1, result.size)
        assertEquals("Peito", result.first().name)
        verify { repository.getMuscleGroups() }
    }
}
