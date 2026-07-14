package com.example.evofit.presentation.ui.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.presentation.ui.feature.profile.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getOnboardingDataUseCase().collect { userData ->
                _uiState.update {
                    it.copy(
                        name = userData.name,
                        age = userData.age,
                        weight = userData.weight,
                        isLoading = false
                    )
                }
            }
        }
    }
}