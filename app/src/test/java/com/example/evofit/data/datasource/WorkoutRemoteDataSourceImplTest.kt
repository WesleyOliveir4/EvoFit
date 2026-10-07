package com.example.evofit.data.datasource

import com.example.evofit.core.monitoring.CrashReporter
import com.example.evofit.domain.model.WorkoutDone
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WorkoutRemoteDataSourceImplTest {

    private val firestore: FirebaseFirestore = mockk(relaxed = true)
    private val crashReporter: CrashReporter = mockk(relaxed = true)
    private val dataSource = WorkoutRemoteDataSourceImpl(firestore, crashReporter)

    private val usersCollection: CollectionReference = mockk(relaxed = true)
    private val userDoc: DocumentReference = mockk(relaxed = true)
    private val historyCollection: CollectionReference = mockk(relaxed = true)
    private val taskQuerySnapshot: Task<QuerySnapshot> = mockk(relaxed = true)
    private val querySnapshot: QuerySnapshot = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        every { firestore.collection("users") } returns usersCollection
        every { usersCollection.document(any()) } returns userDoc
        every { userDoc.collection("history") } returns historyCollection
    }

    @After
    fun tearDown() {
        unmockkStatic("kotlinx.coroutines.tasks.TasksKt")
    }

    @Test
    fun `getLatestWorkoutDoneHistory should return empty list on exception and record crash`() = runBlocking {
        every { historyCollection.whereEqualTo("isDeleted", false) } throws RuntimeException("Network error")

        val history = dataSource.getLatestWorkoutDoneHistory("u1", 10)

        assertTrue(history.isEmpty())
    }
}
