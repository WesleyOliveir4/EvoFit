package com.example.evofit.data.datasource

import com.example.evofit.core.monitoring.CrashReporter
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class UserRemoteDataSourceImplTest {

    private val firestore: FirebaseFirestore = mockk(relaxed = true)
    private val crashReporter: CrashReporter = mockk(relaxed = true)
    private val dataSource = UserRemoteDataSourceImpl(firestore, crashReporter)

    private val usersCollection: CollectionReference = mockk(relaxed = true)
    private val userDoc: DocumentReference = mockk(relaxed = true)
    private val taskDocSnapshot: Task<DocumentSnapshot> = mockk(relaxed = true)
    private val documentSnapshot: DocumentSnapshot = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        every { firestore.collection("users") } returns usersCollection
        every { usersCollection.document(any()) } returns userDoc
    }

    @After
    fun tearDown() {
        unmockkStatic("kotlinx.coroutines.tasks.TasksKt")
    }

    @Test
    fun `getUser should map user data defensively when document exists`() = runBlocking {
        every { userDoc.get() } returns taskDocSnapshot
        coEvery { taskDocSnapshot.await() } returns documentSnapshot
        every { documentSnapshot.exists() } returns true
        every { documentSnapshot.data } returns mapOf(
            "id" to "u123",
            "name" to "John",
            "birthDate" to "01/01/1990",
            "weight" to "80",
            "height" to "180",
            "onboardingCompleted" to true,
            "updatedAt" to 1000L
        )

        val user = dataSource.getUser("u123")

        assertNotNull(user)
        assertEquals("u123", user?.id)
        assertEquals("John", user?.name)
        assertEquals(true, user?.onboardingCompleted)
    }

    @Test
    fun `getUser should return null when document does not exist`() = runBlocking {
        every { userDoc.get() } returns taskDocSnapshot
        coEvery { taskDocSnapshot.await() } returns documentSnapshot
        every { documentSnapshot.exists() } returns false

        val user = dataSource.getUser("non_existent")

        assertNull(user)
    }

    @Test
    fun `getUser should record exception and return null on failure`() = runBlocking {
        every { userDoc.get() } throws RuntimeException("Firestore error")

        val user = dataSource.getUser("u123")

        assertNull(user)
    }
}
