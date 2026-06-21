package com.example.evofit.presentation.ui.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: OnboardingRepository
) : ViewModel() {

    val userData: StateFlow<UserOnboardingData> = repository.getUserData()
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
