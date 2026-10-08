package com.guaracode.evofit.domain.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAppVersionUseCaseTest {

    private val useCase: GetAppVersionUseCase = mockk()

    @Test
    fun `invoke should return app version string`() {
        every { useCase() } returns "1.0.1"

        val result = useCase()

        assertEquals("1.0.1", result)
    }
}
