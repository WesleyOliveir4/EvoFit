package com.example.evofit.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.evofit.core.monitoring.CrashReporter
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkConstructor
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SupportRepositoryImplTest {

    private val context: Context = mockk(relaxed = true)
    private val crashReporter: CrashReporter = mockk(relaxed = true)
    private val repository = SupportRepositoryImpl(context, crashReporter)

    @Before
    fun setUp() {
        mockkStatic(Uri::class)
        mockkConstructor(Intent::class)
        every { Uri.parse(any()) } returns mockk(relaxed = true)
        every { anyConstructed<Intent>().setData(any()) } returns mockk(relaxed = true)
        every { anyConstructed<Intent>().putExtra(any<String>(), any<Array<String>>()) } returns mockk(relaxed = true)
        every { anyConstructed<Intent>().putExtra(any<String>(), any<String>()) } returns mockk(relaxed = true)
        every { anyConstructed<Intent>().addFlags(any()) } returns mockk(relaxed = true)
        every { anyConstructed<Intent>().resolveActivity(any()) } returns mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkConstructor(Intent::class)
        unmockkStatic(Uri::class)
    }

    @Test
    fun `sendSupportEmail success should start activity and return success`() = runBlocking {
        every { context.startActivity(any()) } returns Unit

        val result = repository.sendSupportEmail("Feedback", "Hello support")

        assertTrue("Expected success but got: ${result.exceptionOrNull()}", result.isSuccess)
        verify { context.startActivity(any()) }
    }
}
