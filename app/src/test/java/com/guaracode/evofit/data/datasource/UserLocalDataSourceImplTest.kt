package com.guaracode.evofit.data.datasource

import com.guaracode.evofit.data.local.dao.UserDao
import com.guaracode.evofit.data.local.dao.WeightHistoryDao
import com.guaracode.evofit.data.local.entities.UserEntity
import com.guaracode.evofit.data.local.entities.UserGoalEntity
import com.guaracode.evofit.data.local.entities.WeightUpdateEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class UserLocalDataSourceImplTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val weightHistoryDao: WeightHistoryDao = mockk(relaxed = true)
    private val dataSource = UserLocalDataSourceImpl(userDao, weightHistoryDao)

    @Test
    fun `getUser should delegate to userDao`() {
        val user = UserEntity("u1", "Name", "01/01/1990", "70", "170")
        every { userDao.getUser() } returns flowOf(user)

        val result = dataSource.getUser()

        verify { userDao.getUser() }
    }

    @Test
    fun `getUserDirect should delegate to userDao`() = runBlocking {
        val user = UserEntity("u1", "Name", "01/01/1990", "70", "170")
        coEvery { userDao.getUserDirect() } returns user

        val result = dataSource.getUserDirect()

        assertEquals(user, result)
        coVerify { userDao.getUserDirect() }
    }

    @Test
    fun `insertUser should delegate to userDao`() = runBlocking {
        val user = UserEntity("u1", "Name", "01/01/1990", "70", "170")
        coEvery { userDao.insertUser(user) } returns 1L

        val result = dataSource.insertUser(user)

        assertEquals(1L, result)
        coVerify { userDao.insertUser(user) }
    }

    @Test
    fun `saveUserWithGoals should delegate to userDao`() = runBlocking {
        val user = UserEntity("u1", "Name", "01/01/1990", "70", "170")
        val goals = listOf(UserGoalEntity("g1", "u1", "WEIGHT"))
        coEvery { userDao.saveUserWithGoals(user, goals) } returns 1L

        val result = dataSource.saveUserWithGoals(user, goals)

        assertEquals(1L, result)
        coVerify { userDao.saveUserWithGoals(user, goals) }
    }

    @Test
    fun `getWeightHistory should delegate to weightHistoryDao`() {
        val updates = listOf(WeightUpdateEntity("w1", "u1", "70", "01/01/2026"))
        every { weightHistoryDao.getWeightHistory("u1") } returns flowOf(updates)

        dataSource.getWeightHistory("u1")

        verify { weightHistoryDao.getWeightHistory("u1") }
    }

    @Test
    fun `insertWeightUpdate should delegate to weightHistoryDao`() = runBlocking {
        val update = WeightUpdateEntity("w1", "u1", "70", "01/01/2026")
        coEvery { weightHistoryDao.insertWeightUpdate(update) } returns 1L

        val result = dataSource.insertWeightUpdate(update)

        assertEquals(1L, result)
        coVerify { weightHistoryDao.insertWeightUpdate(update) }
    }

    @Test
    fun `nukeUserData should clear both userDao and weightHistoryDao`() = runBlocking {
        dataSource.nukeUserData()

        coVerify { userDao.nukeUserData() }
        coVerify { weightHistoryDao.deleteAllWeightHistory() }
    }

    @Test
    fun `clearSyncableUserData should clear both userDao and weightHistoryDao`() = runBlocking {
        dataSource.clearSyncableUserData()

        coVerify { userDao.clearSyncableUserData() }
        coVerify { weightHistoryDao.deleteAllWeightHistory() }
    }
}
