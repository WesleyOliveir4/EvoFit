package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class GetWorkoutsCountUseCaseTest {

    private val repository: WorkoutRepository = mockk()
    private val useCase = GetWorkoutsCountUseCase(repository)

    @Test
    fun `should count workouts within period`() = runBlocking {
        // Arrange
        val userId = "user123"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        
        val now = Calendar.getInstance()
        
        val recentDate = dateFormat.format(now.time)
        
        val oldCalendar = Calendar.getInstance()
        oldCalendar.add(Calendar.MONTH, -2)
        val oldDate = dateFormat.format(oldCalendar.time)

        val history = listOf(
            createWorkoutDone(userId, recentDate, 1),
            createWorkoutDone(userId, recentDate, 2),
            createWorkoutDone(userId, oldDate, 3)
        )

        coEvery { repository.getWorkoutDoneHistory(userId) } returns history

        // Act & Assert
        assertEquals(2, useCase(userId, "1 mês"))
        assertEquals(3, useCase(userId, "3 meses"))
        assertEquals(3, useCase(userId, "Tudo"))
    }

    private fun createWorkoutDone(userId: String, date: String, id: Long) = WorkoutDone(
        id = id,
        userId = userId,
        name = "Workout $id",
        muscleGroupId = "mg1",
        date = date,
        exercises = emptyList(),
        time = "45:00"
    )
}
