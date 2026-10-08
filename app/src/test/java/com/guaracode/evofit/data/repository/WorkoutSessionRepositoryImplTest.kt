package com.guaracode.evofit.data.repository

import com.guaracode.evofit.data.datasource.WorkoutLocalDataSource
import com.guaracode.evofit.data.local.entities.ActiveSessionEntity
import com.guaracode.evofit.data.local.entities.ActiveSessionSetEntity
import com.guaracode.evofit.data.local.relations.ActiveSessionWithSets
import com.guaracode.evofit.domain.model.CompletedSet
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class WorkoutSessionRepositoryImplTest {

    private val dataSource: WorkoutLocalDataSource = mockk(relaxed = true)
    private val repository = WorkoutSessionRepositoryImpl(dataSource)

    @Test
    fun `getActiveSession should map dataSource active session with sets to domain`() = runBlocking {
        val sessionEntity = ActiveSessionEntity("w1", 1000L)
        val setEntity = ActiveSessionSetEntity(workoutId = "w1", workoutExerciseId = "we1", setNumber = 1)
        val relation = ActiveSessionWithSets(sessionEntity, listOf(setEntity))

        every { dataSource.getActiveSession() } returns flowOf(relation)

        val result = repository.getActiveSession().first()

        assertNotNull(result)
        assertEquals("w1", result?.workoutId)
        assertEquals(1000L, result?.startTime)
        assertEquals(1, result?.completedSets?.size)
        assertEquals("we1", result?.completedSets?.first()?.workoutExerciseId)
    }

    @Test
    fun `getActiveSession should return null when no session active`() = runBlocking {
        every { dataSource.getActiveSession() } returns flowOf(null)

        val result = repository.getActiveSession().first()

        assertNull(result)
    }

    @Test
    fun `startSession should insert new active session with empty sets`() = runBlocking {
        val sessionSlot = slot<ActiveSessionEntity>()
        val setsSlot = slot<List<ActiveSessionSetEntity>>()

        coEvery { dataSource.insertActiveSession(capture(sessionSlot), capture(setsSlot)) } returns Unit

        repository.startSession("w10", 2000L)

        coVerify { dataSource.insertActiveSession(any(), any()) }
        assertEquals("w10", sessionSlot.captured.workoutId)
        assertEquals(2000L, sessionSlot.captured.startTime)
        assertEquals(0, setsSlot.captured.size)
    }

    @Test
    fun `updateCompletedSets should insert updated set entities when session active`() = runBlocking {
        val sessionEntity = ActiveSessionEntity("w10", 2000L)
        val relation = ActiveSessionWithSets(sessionEntity, emptyList())
        every { dataSource.getActiveSession() } returns flowOf(relation)

        val completedSets = listOf(CompletedSet("we1", 1), CompletedSet("we1", 2))

        repository.updateCompletedSets(completedSets)

        coVerify {
            dataSource.insertActiveSession(
                match { it.workoutId == "w10" },
                match { it.size == 2 && it[0].workoutExerciseId == "we1" && it[1].setNumber == 2 }
            )
        }
    }

    @Test
    fun `clearSession should call deleteActiveSession on dataSource`() = runBlocking {
        repository.clearSession()

        coVerify { dataSource.deleteActiveSession() }
    }
}
