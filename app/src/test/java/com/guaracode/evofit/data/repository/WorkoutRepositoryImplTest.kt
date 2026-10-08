package com.guaracode.evofit.data.repository

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.datasource.LocalExerciseDataSource
import com.guaracode.evofit.data.datasource.WorkoutLocalDataSource
import com.guaracode.evofit.data.datasource.WorkoutRemoteDataSource
import com.guaracode.evofit.data.local.entities.WorkoutEntity
import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.model.WorkoutDone
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WorkoutRepositoryImplTest {

    private val workoutDataSource: WorkoutLocalDataSource = mockk(relaxed = true)
    private val exerciseDataSource: LocalExerciseDataSource = mockk(relaxed = true)
    private val workoutRemoteDataSource: WorkoutRemoteDataSource = mockk(relaxed = true)
    private val crashReporter: CrashReporter = mockk(relaxed = true)

    private val repository = WorkoutRepositoryImpl(
        workoutDataSource,
        exerciseDataSource,
        workoutRemoteDataSource,
        crashReporter
    )

    @Test
    fun `getMaxOrderIndex should return value from dataSource`() = runBlocking {
        coEvery { workoutDataSource.getMaxOrderIndex("u1") } returns 3

        val result = repository.getMaxOrderIndex("u1")

        assertEquals(3, result)
        coVerify { workoutDataSource.getMaxOrderIndex("u1") }
    }

    @Test
    fun `getMaxOrderIndex should return -1 when null`() = runBlocking {
        coEvery { workoutDataSource.getMaxOrderIndex("u1") } returns null

        val result = repository.getMaxOrderIndex("u1")

        assertEquals(-1, result)
    }

    @Test
    fun `saveWorkout should insert full workout into local dataSource`() = runBlocking {
        coEvery { workoutDataSource.getMaxOrderIndex("u1") } returns 0
        coEvery { workoutDataSource.insertFullWorkout(any(), any(), any()) } returns "new_id"

        val workout = Workout(
            id = "",
            userId = "u1",
            name = "Treino A",
            date = "01/01/2026",
            exercisesByGroup = emptyList(),
            orderIndex = 0
        )

        val workoutId = repository.saveWorkout(workout)

        assertNotNull(workoutId)
        coVerify { workoutDataSource.insertFullWorkout(any(), any(), any()) }
    }

    @Test
    fun `saveWorkoutDone should insert workout done into local dataSource`() = runBlocking {
        val workoutDone = WorkoutDone(
            id = "wd1",
            userId = "u1",
            name = "Treino Concluído",
            date = "01/01/2026",
            exercisesByGroup = emptyList(),
            time = "30:00"
        )

        repository.saveWorkoutDone("u1", workoutDone)

        coVerify { workoutDataSource.insertWorkoutDone(any()) }
    }

    @Test
    fun `deleteWorkoutDone should soft delete workout done in local dataSource`() = runBlocking {
        repository.deleteWorkoutDone("u1", "wd1")

        coVerify { workoutDataSource.softDeleteWorkoutDone("wd1", any()) }
    }
}
