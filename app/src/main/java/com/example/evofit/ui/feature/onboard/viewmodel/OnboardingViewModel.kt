package com.example.evofit.ui.feature.onboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val repository: OnboardingRepository,
    private val getExerciseDataUseCase: GetExerciseDataUseCase
) : ViewModel() {

    private val _userData = MutableStateFlow(UserOnboardingData())
    val userData: StateFlow<UserOnboardingData> = _userData.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUserData().collect { data ->
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
            repository.saveUserData(_userData.value)
            onSuccess()
        }
    }

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.saveUserData(_userData.value)
            repository.completeOnboarding()
            onSuccess()
        }
    }

    fun getMuscleGroups() = getExerciseDataUseCase.getMuscleGroups()
    fun getExercisesByGroup(groupId: String) = getExerciseDataUseCase.getExercisesByGroup(groupId)
    fun getSuggestions() = getExerciseDataUseCase.getSuggestions()
}
