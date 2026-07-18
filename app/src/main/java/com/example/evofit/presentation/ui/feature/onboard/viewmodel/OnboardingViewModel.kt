package com.example.evofit.presentation.ui.feature.onboard.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.usecase.*
import com.example.evofit.presentation.mapper.toUiModel
import com.example.evofit.presentation.ui.feature.onboard.state.OnboardingUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val saveOnboardingDataUseCase: SaveOnboardingDataUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase,
    private val getGoalSuggestionsUseCase: GetGoalSuggestionsUseCase,
    private val appContext: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_NAME = "onboard_name"
        private const val KEY_AGE = "onboard_age"
        private const val KEY_WEIGHT = "onboard_weight"
        private const val KEY_HEIGHT = "onboard_height"
    }

    private val _userData = MutableStateFlow(
        UserOnboardingData(
            name = savedStateHandle[KEY_NAME] ?: "",
            age = savedStateHandle[KEY_AGE] ?: "",
            weight = savedStateHandle[KEY_WEIGHT] ?: "",
            height = savedStateHandle[KEY_HEIGHT] ?: "",
            goals = emptyList()
        )
    )

    val uiState: StateFlow<OnboardingUiState> = _userData
        .map { data ->
            OnboardingUiState(
                name = data.name,
                age = data.age,
                weight = data.weight,
                height = data.height,
                goals = data.goals.map { it.toUiModel(appContext) }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OnboardingUiState()
        )

    init {
        loadSavedData()
    }

    private fun loadSavedData() {
        viewModelScope.launch {
            getOnboardingDataUseCase().take(1).collect { data ->
                _userData.update { current ->
                    current.copy(
                        name = current.name.ifBlank { data.name },
                        age = current.age.ifBlank { data.age },
                        weight = current.weight.ifBlank { data.weight },
                        height = current.height.ifBlank { data.height },
                        goals = data.goals
                    )
                }
            }
        }
    }

    fun updateProfile(
        name: String = _userData.value.name,
        age: String = _userData.value.age,
        weight: String = _userData.value.weight,
        height: String = _userData.value.height
    ) {
        savedStateHandle[KEY_NAME] = name
        savedStateHandle[KEY_AGE] = age
        savedStateHandle[KEY_WEIGHT] = weight
        savedStateHandle[KEY_HEIGHT] = height

        _userData.update { it.copy(name = name, age = age, weight = weight, height = height) }
    }

    fun addGoal(goal: UserGoal) {
        _userData.update { it.copy(goals = it.goals + goal) }
    }

    fun removeGoal(goalId: String) {
        _userData.update { it.copy(goals = it.goals.filter { g -> g.id != goalId }) }
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

    fun getMuscleGroups() = getMuscleGroupsUseCase()
    fun getExercisesByGroup(groupId: String) = getExercisesByGroupUseCase(groupId)
    fun getSuggestions() = getGoalSuggestionsUseCase()
}
