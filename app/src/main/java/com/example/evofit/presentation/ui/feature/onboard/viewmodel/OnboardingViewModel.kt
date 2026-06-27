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

    private val _userData = MutableStateFlow(UserOnboardingData.empty())
    val userData: StateFlow<UserOnboardingData> = _userData.asStateFlow()

    init {
        loadSavedData()
    }

    private fun loadSavedData() {
        viewModelScope.launch {
            getOnboardingDataUseCase().take(1).collect { data ->
                _userData.value = data
            }
        }
    }

    fun updateProfile(
        name: String = _userData.value.name,
        age: String = _userData.value.age,
        weight: String = _userData.value.weight,
        height: String = _userData.value.height
    ) {
        _userData.update { it.copy(name = name, age = age, weight = weight, height = height) }
    }

    fun addGoal(goal: UserGoal) {
        _userData.update { it.copy(goals = it.goals + goal) }
    }

    fun removeGoal(goal: UserGoal) {
        _userData.update { it.copy(goals = it.goals.filter { g -> g.id != goal.id }) }
    }

    fun saveAndNext(onContinue: () -> Unit) {
        viewModelScope.launch {
            saveOnboardingDataUseCase(_userData.value)
            onContinue()
        }
    }

    fun finishOnboarding(onFinish: () -> Unit) {
        viewModelScope.launch {
            completeOnboardingUseCase(_userData.value)
            onFinish()
        }
    }

    fun getMuscleGroups() = getExerciseDataUseCase.getMuscleGroups()
    fun getExercisesByGroup(groupId: String) = getExerciseDataUseCase.getExercisesByGroup(groupId)
    fun getSuggestions() = getExerciseDataUseCase.getSuggestions()
}
