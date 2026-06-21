package com.example.evofit.presentation.ui.feature.onboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.usecase.CompleteOnboardingUseCase
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.SaveOnboardingDataUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val saveOnboardingDataUseCase: SaveOnboardingDataUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val getExerciseDataUseCase: GetExerciseDataUseCase
) : ViewModel() {

    private val _userData = MutableStateFlow(
        UserOnboardingData(
            name = "",
            age = "",
            weight = "",
            height = "",
            goals = emptyList()
        )
    )
    val userData: StateFlow<UserOnboardingData> = _userData.asStateFlow()

    init {
        viewModelScope.launch {
            getOnboardingDataUseCase().collect { data ->
                _userData.value = data
            }
        }
    }

    fun updateName(name: String) {
        _userData.update { it.copy(name = name) }
    }

    fun updateAge(age: String) {
        _userData.update { it.copy(age = age) }
    }

    fun updateWeight(weight: String) {
        _userData.update { it.copy(weight = weight) }
    }

    fun updateHeight(height: String) {
        _userData.update { it.copy(height = height) }
    }

    fun addGoal(goal: UserGoal) {
        _userData.update { 
            it.copy(goals = it.goals + goal)
        }
    }

    fun removeGoal(goal: UserGoal) {
        _userData.update { 
            it.copy(goals = it.goals.filter { g -> g.id != goal.id })
        }
    }

    fun saveAndNext(onSuccess: () -> Unit) {
        viewModelScope.launch {
            saveOnboardingDataUseCase(_userData.value)
            onSuccess()
        }
    }

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            completeOnboardingUseCase(_userData.value)
            onSuccess()
        }
    }

    fun getMuscleGroups() = getExerciseDataUseCase.getMuscleGroups()
    fun getExercisesByGroup(groupId: String) = getExerciseDataUseCase.getExercisesByGroup(groupId)
    fun getSuggestions() = getExerciseDataUseCase.getSuggestions()
}
