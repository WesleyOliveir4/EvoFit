package com.example.evofit.presentation.ui.feature.profile.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.domain.usecase.GetWorkoutsCountUseCase
import com.example.evofit.domain.usecase.LogoutUseCase
import com.example.evofit.presentation.ui.feature.profile.home.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getWorkoutsCountUseCase: GetWorkoutsCountUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = getUserIdUseCase() ?: ""
            
            combine(
                getOnboardingDataUseCase(),
                kotlinx.coroutines.flow.flow { emit(getWorkoutDoneHistoryUseCase(userId)) }
            ) { userData, history ->
                val totalWorkouts = getWorkoutsCountUseCase(history).toString()
                
                // For records, we can count unique exercises performed as a simple metric for now
                // or specific records if available. Here we use unique exercise count.
                val uniqueExercises = history.flatMap { it.exercises }.map { it.exerciseId }.distinct().size.toString()
                val goalsCount = userData.goals.size.toString()

                _uiState.update {
                    it.copy(
                        name = getFirstName(userData.name),
                        age = userData.age,
                        weight = userData.weight,
                        totalWorkouts = totalWorkouts,
                        records = uniqueExercises,
                        goals = goalsCount,
                        isLoading = false
                    )
                }
            }.collect({})
        }
    }

    private fun getFirstName(fullName: String): String {
        return fullName.trim().split("\\s+".toRegex()).firstOrNull() ?: fullName
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                _uiState.update { it.copy(isLoggedOut = true) }
            }
        }
    }
}