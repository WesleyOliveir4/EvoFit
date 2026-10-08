package com.guaracode.evofit.data.local.session

import android.content.Context
import io.mockk.mockk
import org.junit.Test

class SessionManagerTest {

    private val context: Context = mockk(relaxed = true)

    // Note: SessionManager uses extension function context.dataStore.
    // For unit testing DataStore logic or testing data mapping, we can test flow transformations.
    // Since preferencesDataStore relies on Context, we can mock the DataStore instance if needed.

    @Test
    fun `SessionManager default initial state flows can be verified`() {
        // Just verify SessionManager class instantiation and methods signatures
        val sessionManager = SessionManager(context)
        org.junit.Assert.assertNotNull(sessionManager)
    }
}
