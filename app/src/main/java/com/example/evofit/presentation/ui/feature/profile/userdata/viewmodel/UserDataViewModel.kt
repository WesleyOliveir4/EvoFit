package com.example.evofit.presentation.ui.feature.profile.userdata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.usecase.CompleteOnboardingUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.presentation.ui.feature.profile.userdata.state.UserDataUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDataViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDataUiState())
    val uiState: StateFlow<UserDataUiState> = _uiState.asStateFlow()

    private var currentData = UserOnboardingData.empty()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getOnboardingDataUseCase().collect { data ->
                currentData = data
                _uiState.update {
                    it.copy(
                        name = data.name,
                        age = data.age,
                        weight = data.weight,
                        height = data.height,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateUserData(name: String, age: String, weight: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val updatedData = currentData.copy(
                name = name,
                age = age,
                weight = weight
            )
            completeOnboardingUseCase(updatedData)
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }

    fun resetSavedState() {
        _uiState.update { it.copy(isSaved = false) }
    }
}