package com.example.evofit.data.mapper

import com.example.evofit.data.model.ExerciseModel
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.data.model.MuscleGroupType
import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.MuscleGroupType as DomainMuscleGroupType
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseMapperTest {

    @Test
    fun `MuscleGroupModel toDomain should map all fields correctly`() {
        val model = MuscleGroupModel(
            id = "1",
            name = "Peito",
            type = MuscleGroupType.CHEST,
            category = ExerciseCategory.STRENGTH
        )

        val domain = model.toDomain()

        assertEquals("1", domain.id)
        assertEquals("Peito", domain.name)
        assertEquals(DomainMuscleGroupType.CHEST, domain.type)
        assertEquals(ExerciseCategory.STRENGTH, domain.category)
    }

    @Test
    fun `MuscleGroupType toDomain should map all types correctly`() {
        val mappings = mapOf(
            MuscleGroupType.CHEST to DomainMuscleGroupType.CHEST,
            MuscleGroupType.BACK to DomainMuscleGroupType.BACK,
            MuscleGroupType.SHOULDERS to DomainMuscleGroupType.SHOULDERS,
            MuscleGroupType.BICEPS to DomainMuscleGroupType.BICEPS,
            MuscleGroupType.TRICEPS to DomainMuscleGroupType.TRICEPS,
            MuscleGroupType.FOREARMS to DomainMuscleGroupType.FOREARMS,
            MuscleGroupType.LEGS to DomainMuscleGroupType.LEGS,
            MuscleGroupType.ABS to DomainMuscleGroupType.ABS,
            MuscleGroupType.CARDIO to DomainMuscleGroupType.CARDIO,
            MuscleGroupType.GLUTES to DomainMuscleGroupType.GLUTES,
            MuscleGroupType.CALVES to DomainMuscleGroupType.CALVES,
            MuscleGroupType.OTHER to DomainMuscleGroupType.OTHER
        )

        for ((dataType, expectedDomainType) in mappings) {
            assertEquals(expectedDomainType, dataType.toDomain())
        }
    }

    @Test
    fun `ExerciseModel toDomain should map all fields correctly`() {
        val model = ExerciseModel(
            id = "101",
            name = "Supino Reto",
            muscleGroupId = "2",
            unit = MeasurementUnit.WEIGHT,
            sortOrder = 1,
            isEnabled = true
        )

        val domain = model.toDomain()

        assertEquals("101", domain.id)
        assertEquals("Supino Reto", domain.name)
        assertEquals("2", domain.muscleGroupId)
        assertEquals(MeasurementUnit.WEIGHT, domain.unit)
        assertEquals(1, domain.sortOrder)
        assertEquals(true, domain.isEnabled)
    }

    @Test
    fun `MuscleGroup toData should map all fields correctly`() {
        val domain = MuscleGroup(
            id = "2",
            name = "Costas",
            type = DomainMuscleGroupType.BACK,
            category = ExerciseCategory.STRENGTH
        )

        val data = domain.toData()

        assertEquals("2", data.id)
        assertEquals("Costas", data.name)
        assertEquals(MuscleGroupType.BACK, data.type)
        assertEquals(ExerciseCategory.STRENGTH, data.category)
    }

    @Test
    fun `DomainMuscleGroupType toData should map all types correctly`() {
        val mappings = mapOf(
            DomainMuscleGroupType.CHEST to MuscleGroupType.CHEST,
            DomainMuscleGroupType.BACK to MuscleGroupType.BACK,
            DomainMuscleGroupType.SHOULDERS to MuscleGroupType.SHOULDERS,
            DomainMuscleGroupType.BICEPS to MuscleGroupType.BICEPS,
            DomainMuscleGroupType.TRICEPS to MuscleGroupType.TRICEPS,
            DomainMuscleGroupType.FOREARMS to MuscleGroupType.FOREARMS,
            DomainMuscleGroupType.LEGS to MuscleGroupType.LEGS,
            DomainMuscleGroupType.ABS to MuscleGroupType.ABS,
            DomainMuscleGroupType.CARDIO to MuscleGroupType.CARDIO,
            DomainMuscleGroupType.GLUTES to MuscleGroupType.GLUTES,
            DomainMuscleGroupType.CALVES to MuscleGroupType.CALVES,
            DomainMuscleGroupType.OTHER to MuscleGroupType.OTHER
        )

        for ((domainType, expectedDataType) in mappings) {
            assertEquals(expectedDataType, domainType.toData())
        }
    }
}
