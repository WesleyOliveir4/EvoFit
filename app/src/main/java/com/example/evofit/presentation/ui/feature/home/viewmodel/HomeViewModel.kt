package com.example.evofit.presentation.ui.feature.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.presentation.mapper.toUiModel
import com.example.evofit.presentation.ui.feature.home.state.HomeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val appContext: Context
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = getOnboardingDataUseCase()
        .map { userData ->
            HomeUiState(
                userName = userData.name,
                goals = userData.goals.map { it.toUiModel(appContext) }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )
}
