package com.example.evofit.presentation.ui.feature.onboard.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.usecase.CompleteOnboardingUseCase
import com.example.evofit.domain.usecase.GetExercisesByGroupUseCase
import com.example.evofit.domain.usecase.GetGoalSuggestionsUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.SaveOnboardingDataUseCase
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
    private val appContext: Context
) : ViewModel() {

    // Estado interno "cru", usado apenas para montar o payload de salvamento
    // e para a lógica de criação de metas. Nunca exposto diretamente à UI.
    private val _userData = MutableStateFlow(UserOnboardingData.empty())

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
