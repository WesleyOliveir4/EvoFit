package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.domain.model.WeightUpdate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ProcessBodyWeightAnalyticsUseCaseTest {

    private val useCase = ProcessBodyWeightAnalyticsUseCaseImpl()

    @Test
    fun `when weight history is empty, should return null`() {
        assertNull(useCase(emptyList()))
    }

    @Test
    fun `should process body weight analytics and return data points`() {
        val history = listOf(
            WeightUpdate("w1", "80.5", "01/01/2026"),
            WeightUpdate("w2", "78.0", "01/02/2026")
        )

        val result = useCase(history)

        assertNotNull(result)
        assertEquals(MeasurementUnit.WEIGHT, result?.unit)
        assertEquals("80.5kg", result?.maxRecord)
        assertEquals("78.0kg", result?.secondaryRecord)
        assertEquals(2, result?.loadChartPoints?.size)
    }
}
