package com.example.evofit.presentation.ui.feature.profile.about.viewmodel

import com.example.evofit.domain.usecase.GetAppVersionUseCase
import com.example.evofit.presentation.ui.feature.profile.about.tracking.AboutAppTracker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AboutAppViewModelTest {

    private val getAppVersionUseCase: GetAppVersionUseCase = mockk()
    private val tracker: AboutAppTracker = mockk(relaxed = true)

    private lateinit var viewModel: AboutAppViewModel

    @Before
    fun setUp() {
        every { getAppVersionUseCase() } returns "1.0.1"
        viewModel = AboutAppViewModel(getAppVersionUseCase, tracker)
    }

    @Test
    fun `uiState should contain app version from usecase`() {
        val currentState = viewModel.uiState.value
        assertEquals("1.0.1", currentState.appVersion)
    }

    @Test
    fun `trackScreenView should call tracker trackAboutAppScreenView`() {
        viewModel.trackScreenView()
        verify { tracker.trackAboutAppScreenView() }
    }
}
