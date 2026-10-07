package com.example.evofit.data.mapper

import com.example.evofit.data.local.entities.UserEntity
import com.example.evofit.data.local.entities.UserGoalEntity
import com.example.evofit.data.local.entities.WeightUpdateEntity
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.model.WeightUpdate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class UserMapperTest {

    @Test
    fun `WeightUpdate toEntity should set userId and preserve fields`() {
        val domain = WeightUpdate(id = "w1", weight = "75.0", date = "20/01/2026")
        val entity = domain.toEntity("user123")

        assertEquals("w1", entity.id)
        assertEquals("user123", entity.userId)
        assertEquals("75.0", entity.weight)
        assertEquals("20/01/2026", entity.date)
    }

    @Test
    fun `WeightUpdate toEntity should generate UUID when id is blank`() {
        val domain = WeightUpdate(id = "", weight = "75.0", date = "20/01/2026")
        val entity = domain.toEntity("user123")

        assertNotNull(entity.id)
        assertFalse(entity.id.isBlank())
    }

    @Test
    fun `WeightUpdateEntity toDomain should map correctly`() {
        val entity = WeightUpdateEntity(
            id = "w1",
            userId = "user123",
            weight = "80.0",
            date = "15/02/2026",
            timestamp = 1000L
        )
        val domain = entity.toDomain()

        assertEquals("w1", domain.id)
        assertEquals("80.0", domain.weight)
        assertEquals("15/02/2026", domain.date)
    }

    @Test
    fun `UserOnboardingData toEntity should map to UserEntity correctly`() {
        val onboarding = UserOnboardingData(
            name = "John Doe",
            birthDate = "01/01/1990",
            weight = "80.0",
            height = "180",
            profilePictureUri = "uri/path",
            goals = emptyList()
        )

        val entity = onboarding.toEntity("user123")

        assertEquals("user123", entity.id)
        assertEquals("John Doe", entity.name)
        assertEquals("01/01/1990", entity.birthDate)
        assertEquals("80.0", entity.weight)
        assertEquals("180", entity.height)
        assertEquals("uri/path", entity.profilePictureUri)
        assertFalse(entity.onboardingCompleted)
    }

    @Test
    fun `UserGoal Strength toEntity should map correctly`() {
        val goal = UserGoal.Strength(
            id = "g1",
            exerciseName = "Supino",
            value = "100",
            unit = MeasurementUnit.WEIGHT
        )

        val entity = goal.toEntity("user123")

        assertEquals("g1", entity.id)
        assertEquals("user123", entity.userId)
        assertEquals("STRENGTH", entity.type)
        assertEquals("Supino", entity.exerciseName)
        assertEquals("100", entity.value)
        assertEquals("WEIGHT", entity.unit)
        assertNull(entity.cardioType)
        assertNull(entity.distance)
        assertNull(entity.time)
    }

    @Test
    fun `UserGoal Cardio toEntity should map correctly`() {
        val goal = UserGoal.Cardio(
            id = "g2",
            type = "Corrida",
            distance = "5.0",
            time = "30:00"
        )

        val entity = goal.toEntity("user123")

        assertEquals("g2", entity.id)
        assertEquals("user123", entity.userId)
        assertEquals("CARDIO", entity.type)
        assertNull(entity.exerciseName)
        assertNull(entity.value)
        assertNull(entity.unit)
        assertEquals("Corrida", entity.cardioType)
        assertEquals("5.0", entity.distance)
        assertEquals("30:00", entity.time)
    }

    @Test
    fun `UserGoal Weight toEntity should map correctly`() {
        val goal = UserGoal.Weight(
            id = "g3",
            targetWeight = "70.0"
        )

        val entity = goal.toEntity("user123")

        assertEquals("g3", entity.id)
        assertEquals("user123", entity.userId)
        assertEquals("WEIGHT", entity.type)
        assertNull(entity.exerciseName)
        assertEquals("70.0", entity.value)
        assertNull(entity.unit)
        assertNull(entity.cardioType)
        assertNull(entity.distance)
        assertNull(entity.time)
    }

    @Test
    fun `UserGoalEntity toDomain STRENGTH should map correctly`() {
        val entity = UserGoalEntity(
            id = "g1",
            userId = "user123",
            type = "STRENGTH",
            exerciseName = "Agachamento",
            value = "120",
            unit = "WEIGHT"
        )

        val domain = entity.toDomain() as UserGoal.Strength

        assertEquals("g1", domain.id)
        assertEquals("Agachamento", domain.exerciseName)
        assertEquals("120", domain.value)
        assertEquals(MeasurementUnit.WEIGHT, domain.unit)
    }

    @Test
    fun `UserGoalEntity toDomain CARDIO should map correctly`() {
        val entity = UserGoalEntity(
            id = "g2",
            userId = "user123",
            type = "CARDIO",
            cardioType = "Bicicleta",
            distance = "10",
            time = "45:00"
        )

        val domain = entity.toDomain() as UserGoal.Cardio

        assertEquals("g2", domain.id)
        assertEquals("Bicicleta", domain.type)
        assertEquals("10", domain.distance)
        assertEquals("45:00", domain.time)
    }

    @Test
    fun `UserGoalEntity toDomain WEIGHT should map correctly`() {
        val entity = UserGoalEntity(
            id = "g3",
            userId = "user123",
            type = "WEIGHT",
            value = "75.0"
        )

        val domain = entity.toDomain() as UserGoal.Weight

        assertEquals("g3", domain.id)
        assertEquals("75.0", domain.targetWeight)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `UserGoalEntity toDomain with unknown type should throw IllegalArgumentException`() {
        val entity = UserGoalEntity(
            id = "g4",
            userId = "user123",
            type = "UNKNOWN"
        )

        entity.toDomain()
    }

    @Test
    fun `mapToDomain should map user and goals correctly`() {
        val userEntity = UserEntity(
            id = "u1",
            name = "Jane",
            birthDate = "10/10/1995",
            weight = "60.0",
            height = "165",
            profilePictureUri = "pic.jpg",
            onboardingCompleted = true
        )
        val goalEntities = listOf(
            UserGoalEntity(
                id = "g1",
                userId = "u1",
                type = "WEIGHT",
                value = "58.0"
            )
        )

        val domain = mapToDomain(userEntity, goalEntities)

        assertEquals("Jane", domain.name)
        assertEquals("10/10/1995", domain.birthDate)
        assertEquals("60.0", domain.weight)
        assertEquals("165", domain.height)
        assertEquals("pic.jpg", domain.profilePictureUri)
        assertEquals(1, domain.goals.size)
        assertEquals("58.0", (domain.goals.first() as UserGoal.Weight).targetWeight)
    }
}
