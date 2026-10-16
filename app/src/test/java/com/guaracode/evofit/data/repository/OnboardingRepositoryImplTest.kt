package com.guaracode.evofit.data.repository

import android.content.Context
import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.datasource.LocalExerciseDataSource
import com.guaracode.evofit.data.datasource.UserLocalDataSource
import com.guaracode.evofit.data.datasource.UserRemoteDataSource
import com.guaracode.evofit.data.datasource.WorkoutLocalDataSource
import com.guaracode.evofit.data.datasource.WorkoutRemoteDataSource
import com.guaracode.evofit.data.local.entities.UserEntity
import com.guaracode.evofit.data.local.session.SessionManager
import com.guaracode.evofit.domain.model.UserOnboardingData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingRepositoryImplTest {

    private val userDataSource: UserLocalDataSource = mockk(relaxed = true)
    private val userRemoteDataSource: UserRemoteDataSource = mockk(relaxed = true)
    private val workoutLocalDataSource: WorkoutLocalDataSource = mockk(relaxed = true)
    private val workoutRemoteDataSource: WorkoutRemoteDataSource = mockk(relaxed = true)
    private val exerciseDataSource: LocalExerciseDataSource = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)
    private val crashReporter: CrashReporter = mockk(relaxed = true)

    private val repository = OnboardingRepositoryImpl(
        userDataSource,
        userRemoteDataSource,
        workoutLocalDataSource,
        workoutRemoteDataSource,
        exerciseDataSource,
        sessionManager,
        context,
        crashReporter
    )

    @Test
    fun `getUserData should return null flow when user entity is null`() = runBlocking {
        every { userDataSource.getUser() } returns flowOf(null)

        val result = repository.getUserData().first()

        assertNull(result)
        verify { userDataSource.getUser() }
    }

    @Test
    fun `getUserId should return id flow from user entity`() = runBlocking {
        val userEntity = UserEntity("u123", "User", "01/01/1990", "80", "180")
        every { userDataSource.getUser() } returns flowOf(userEntity)

        val result = repository.getUserId().first()

        assertEquals("u123", result)
    }

    @Test
    fun `saveUserData should save user and goals locally`() = runBlocking {
        val onboardingData = UserOnboardingData(
            name = "John",
            birthDate = "01/01/1990",
            weight = "80",
            height = "180",
            goals = emptyList()
        )

        repository.saveUserData(onboardingData, "u123", isCompleted = true)

        coVerify { userDataSource.saveUserWithGoals(match { it.id == "u123" && it.onboardingCompleted }, any()) }
    }

    @Test
    fun `isOnboardingCompleted should emit boolean status from user entity`() = runBlocking {
        val userEntity = UserEntity("u123", "User", "01/01/1990", "80", "180", onboardingCompleted = true)
        every { userDataSource.getUser() } returns flowOf(userEntity)

        val result = repository.isOnboardingCompleted().first()

        assertTrue(result)
    }

    @Test
    fun `isOnboardingCompletedDirect should return direct boolean status`() = runBlocking {
        val userEntity = UserEntity("u123", "User", "01/01/1990", "80", "180", onboardingCompleted = false)
        coEvery { userDataSource.getUserDirect() } returns userEntity

        val result = repository.isOnboardingCompletedDirect()

        assertFalse(result)
        coVerify { userDataSource.getUserDirect() }
    }

    @Test
    fun `nukeUserData should clear local user data and reset session sync time`() = runBlocking {
        repository.nukeUserData()

        coVerify { userDataSource.nukeUserData() }
        coVerify { sessionManager.updateSyncTime(0L) }
    }
}
