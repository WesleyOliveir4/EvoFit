package com.guaracode.evofit.presentation.ui.feature.onboard.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guaracode.evofit.domain.model.UserGoal
import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.usecase.*
import com.guaracode.evofit.presentation.mapper.toUiModel
import com.guaracode.evofit.presentation.ui.feature.onboard.state.OnboardingUiState
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val saveOnboardingDataUseCase: SaveOnboardingDataUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val addWeightUpdateUseCase: AddWeightUpdateUseCase,
    private val getGoalSuggestionsUseCase: GetGoalSuggestionsUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val tracker: OnboardingTracker
) : ViewModel() {

    companion object {
        private const val KEY_NAME = "onboard_name"
        private const val KEY_BIRTH_DATE = "onboard_birth_date"
        private const val KEY_WEIGHT = "onboard_weight"
        private const val KEY_HEIGHT = "onboard_height"
    }

    private val _userData = MutableStateFlow(
        UserOnboardingData(
            name = savedStateHandle[KEY_NAME] ?: "",
            birthDate = savedStateHandle[KEY_BIRTH_DATE] ?: "",
            weight = savedStateHandle[KEY_WEIGHT] ?: "",
            height = savedStateHandle[KEY_HEIGHT] ?: "",
            goals = emptyList()
        )
    )

    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<OnboardingUiState> = combine(_userData, _isLoading) { data, loading ->
        OnboardingUiState(
            name = data.name,
            birthDate = data.birthDate,
            weight = data.weight,
            height = data.height,
            goals = data.goals.map { it.toUiModel() },
            isLoading = loading
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
                        birthDate = current.birthDate.ifBlank { data.birthDate },
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
        birthDate: String = _userData.value.birthDate,
        weight: String = _userData.value.weight,
        height: String = _userData.value.height
    ) {
        savedStateHandle[KEY_NAME] = name
        savedStateHandle[KEY_BIRTH_DATE] = birthDate
        savedStateHandle[KEY_WEIGHT] = weight
        savedStateHandle[KEY_HEIGHT] = height

        _userData.update { it.copy(name = name, birthDate = birthDate, weight = weight, height = height) }
    }

    fun addGoal(goal: UserGoal) {
        tracker.trackGoalAdded(goal::class.simpleName ?: "Unknown")
        _userData.update { it.copy(goals = it.goals + goal) }
    }

    fun removeGoal(goalId: String) {
        tracker.trackGoalRemoved(goalId)
        _userData.update { it.copy(goals = it.goals.filter { g -> g.id != goalId }) }
    }

    fun saveAndNext(onContinue: () -> Unit) {
        viewModelScope.launch {
            saveOnboardingDataUseCase(_userData.value)
            onContinue()
        }
    }

    fun finishOnboarding(onFinish: () -> Unit) {
        if (_isLoading.value) return
        
        _isLoading.value = true
        viewModelScope.launch {
            try {
                tracker.trackOnboardingCompleted(_userData.value.goals.size)
                completeOnboardingUseCase(_userData.value)
                if (_userData.value.weight.isNotBlank()) {
                    addWeightUpdateUseCase(_userData.value.weight)
                }
                clearCache()
                onFinish()
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun clearCache() {
        savedStateHandle.remove<String>(KEY_NAME)
        savedStateHandle.remove<String>(KEY_BIRTH_DATE)
        savedStateHandle.remove<String>(KEY_WEIGHT)
        savedStateHandle.remove<String>(KEY_HEIGHT)
        _userData.value = UserOnboardingData()
    }

    fun getSuggestions() = getGoalSuggestionsUseCase()
}
