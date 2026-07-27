package com.example.evofit.presentation.ui.feature.profile.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.domain.usecase.GetWorkoutsCountUseCase
import com.example.evofit.domain.usecase.LogoutUseCase
import com.example.evofit.domain.usecase.NukeUserDataUseCase
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
    private val logoutUseCase: LogoutUseCase,
    private val nukeUserDataUseCase: NukeUserDataUseCase
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
                getWorkoutDoneHistoryUseCase(userId)
            ) { userData, history ->
                Log.d("ProfileViewModel", "Dados combinados: birthDate=${userData.birthDate}, historySize=${history.size}")
                val totalWorkouts = getWorkoutsCountUseCase(history).toString()
                val uniqueExercises = history.flatMap { it.exercises }.map { it.exerciseId }.distinct().size.toString()
                val goalsCount = userData.goals.size.toString()

                ProfileUiState(
                    name = getFirstName(userData.name),
                    birthDate = userData.birthDate,
                    weight = userData.weight,
                    totalWorkouts = totalWorkouts,
                    records = uniqueExercises,
                    goals = goalsCount,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun getFirstName(fullName: String): String {
        return fullName.trim().split("\\s+".toRegex()).firstOrNull() ?: fullName
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                nukeUserDataUseCase()
                _uiState.update { it.copy(isLoggedOut = true) }
            }
        }
    }
}