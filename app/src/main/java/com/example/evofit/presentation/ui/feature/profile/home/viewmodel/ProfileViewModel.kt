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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getWorkoutsCountUseCase: GetWorkoutsCountUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val nukeUserDataUseCase: NukeUserDataUseCase
) : ViewModel() {

    private val _isLoggedOut = MutableStateFlow(false)

    val uiState: StateFlow<ProfileUiState> = _isLoggedOut
        .flatMapLatest { isLoggedOut ->
            if (isLoggedOut) {
                flow { emit(ProfileUiState(isLoggedOut = true)) }
            } else {
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
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ProfileUiState(isLoading = true)
        )

    private fun getFirstName(fullName: String): String {
        return fullName.trim().split("\\s+".toRegex()).firstOrNull() ?: fullName
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                nukeUserDataUseCase()
                _isLoggedOut.value = true
            }
        }
    }
}
