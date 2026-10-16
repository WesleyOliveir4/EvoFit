package com.guaracode.evofit.data.datasource

import com.guaracode.evofit.data.local.dao.UserDao
import com.guaracode.evofit.data.local.entities.ActiveSessionEntity
import com.guaracode.evofit.data.local.entities.WorkoutDoneEntity
import com.guaracode.evofit.data.local.entities.WorkoutEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutLocalDataSourceImplTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val dataSource = WorkoutLocalDataSourceImpl(userDao)

    @Test
    fun `getFullWorkouts should delegate to userDao`() {
        every { userDao.getFullWorkouts("u1") } returns flowOf(emptyList())

        dataSource.getFullWorkouts("u1")

        verify { userDao.getFullWorkouts("u1") }
    }

    @Test
    fun `getMaxOrderIndex should delegate to userDao`() = runBlocking {
        coEvery { userDao.getMaxOrderIndex("u1") } returns 5

        val result = dataSource.getMaxOrderIndex("u1")

        assertEquals(5, result)
        coVerify { userDao.getMaxOrderIndex("u1") }
    }

    @Test
    fun `insertFullWorkout should delegate to userDao`() = runBlocking {
        val workout = WorkoutEntity("w1", "u1", "Name", "01/01/2026", 1)
        coEvery { userDao.insertFullWorkoutReturnId(workout, emptyList(), emptyList()) } returns "w1"

        val result = dataSource.insertFullWorkout(workout, emptyList(), emptyList())

        assertEquals("w1", result)
        coVerify { userDao.insertFullWorkoutReturnId(workout, emptyList(), emptyList()) }
    }

    @Test
    fun `insertWorkoutDone should delegate to userDao`() = runBlocking {
        val workoutDone = WorkoutDoneEntity("wd1", "u1", "Name", "01/01/2026", emptyList(), "30:00", 1000L)

        dataSource.insertWorkoutDone(workoutDone)

        coVerify { userDao.insertWorkoutDone(workoutDone) }
    }

    @Test
    fun `getActiveSession should delegate to userDao`() {
        every { userDao.getActiveSessionWithSets() } returns flowOf(null)

        dataSource.getActiveSession()

        verify { userDao.getActiveSessionWithSets() }
    }

    @Test
    fun `insertActiveSession should delegate to userDao updateActiveSession`() = runBlocking {
        val session = ActiveSessionEntity("w1", 1000L)

        dataSource.insertActiveSession(session, emptyList())

        coVerify { userDao.updateActiveSession(session, emptyList()) }
    }

    @Test
    fun `deleteActiveSession should delegate to userDao`() = runBlocking {
        dataSource.deleteActiveSession()

        coVerify { userDao.deleteActiveSession() }
    }
}
