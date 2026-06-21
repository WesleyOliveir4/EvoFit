package com.example.evofit.presentation.ui.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase
) : ViewModel() {

    val userData: StateFlow<UserOnboardingData> = getOnboardingDataUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserOnboardingData(
                name = "",
                age = "",
                weight = "",
                height = "",
                goals = emptyList()
            )
        )
}
