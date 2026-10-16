package com.guaracode.evofit.data.repository

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.local.session.SessionManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private val firebaseAuth: FirebaseAuth = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val crashReporter: CrashReporter = mockk(relaxed = true)
    private val repository = AuthRepositoryImpl(firebaseAuth, sessionManager, crashReporter)

    private val authResultTask: Task<AuthResult> = mockk(relaxed = true)
    private val voidTask: Task<Void> = mockk(relaxed = true)
    private val authResult: AuthResult = mockk(relaxed = true)
    private val firebaseUser: FirebaseUser = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        every { firebaseUser.uid } returns "user123"
        every { authResult.user } returns firebaseUser
    }

    @After
    fun tearDown() {
        unmockkStatic("kotlinx.coroutines.tasks.TasksKt")
    }

    @Test
    fun `register success should save session and set userId on crashReporter`() = runBlocking {
        every { firebaseAuth.createUserWithEmailAndPassword("test@test.com", "123456") } returns authResultTask
        coEvery { authResultTask.await() } returns authResult

        val result = repository.register("test@test.com", "123456")

        assertTrue(result.isSuccess)
        coVerify { sessionManager.saveSession("user123") }
        verify { crashReporter.setUserId("user123") }
    }

    @Test
    fun `register failure should record exception and return failure`() = runBlocking {
        val exception = RuntimeException("Auth error")
        every { firebaseAuth.createUserWithEmailAndPassword("test@test.com", "123456") } throws exception

        val result = repository.register("test@test.com", "123456")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify { crashReporter.recordException(exception) }
    }

    @Test
    fun `login success should save session and set userId`() = runBlocking {
        every { firebaseAuth.signInWithEmailAndPassword("test@test.com", "123456") } returns authResultTask
        coEvery { authResultTask.await() } returns authResult

        val result = repository.login("test@test.com", "123456")

        assertTrue(result.isSuccess)
        coVerify { sessionManager.saveSession("user123") }
        verify { crashReporter.setUserId("user123") }
    }

    @Test
    fun `login failure should record exception and return failure`() = runBlocking {
        val exception = RuntimeException("Wrong password")
        every { firebaseAuth.signInWithEmailAndPassword("test@test.com", "123456") } throws exception

        val result = repository.login("test@test.com", "123456")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `sendPasswordResetCode success should return success`() = runBlocking {
        every { firebaseAuth.sendPasswordResetEmail("test@test.com") } returns voidTask
        coEvery { voidTask.await() } returns mockk()

        val result = repository.sendPasswordResetCode("test@test.com")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `isLoggedIn should return true when currentUser is not null`() {
        every { firebaseAuth.currentUser } returns firebaseUser
        assertTrue(repository.isLoggedIn())

        every { firebaseAuth.currentUser } returns null
        assertFalse(repository.isLoggedIn())
    }

    @Test
    fun `getCurrentUserId should return current user uid`() {
        every { firebaseAuth.currentUser } returns firebaseUser
        assertEquals("user123", repository.getCurrentUserId())
    }

    @Test
    fun `logout success should signOut, clearSession and clear userId on crashReporter`() = runBlocking {
        val result = repository.logout()

        assertTrue(result.isSuccess)
        verify { firebaseAuth.signOut() }
        coVerify { sessionManager.clearSession() }
        verify { crashReporter.setUserId("") }
    }
}
