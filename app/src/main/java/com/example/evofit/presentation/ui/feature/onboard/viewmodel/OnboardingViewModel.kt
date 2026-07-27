package com.example.evofit.presentation.ui.feature.onboard.viewmodel

import android.content.Context
import android.util.Log
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

    val uiState: StateFlow<OnboardingUiState> = _userData
        .map { data ->
            OnboardingUiState(
                name = data.name,
                birthDate = data.birthDate,
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
        // Log para depuração
        Log.d("OnboardingVM", "updateProfile: birthDate=$birthDate")
        
        // Removemos o return precoce para não travar outros campos se a data estiver temporariamente inválida
        // A validação final deve ocorrer no isFormValid da UI ou antes do save

        savedStateHandle[KEY_NAME] = name
        savedStateHandle[KEY_BIRTH_DATE] = birthDate
        savedStateHandle[KEY_WEIGHT] = weight
        savedStateHandle[KEY_HEIGHT] = height

        _userData.update { it.copy(name = name, birthDate = birthDate, weight = weight, height = height) }
    }

    fun addGoal(goal: UserGoal) {
        _userData.update { it.copy(goals = it.goals + goal) }
    }

    fun removeGoal(goalId: String) {
        _userData.update { it.copy(goals = it.goals.filter { g -> g.id != goalId }) }
    }

    fun saveAndNext(onContinue: () -> Unit) {
        viewModelScope.launch {
            Log.d("OnboardingVM", "Salvando dados: birthDate=${_userData.value.birthDate}")
            saveOnboardingDataUseCase(_userData.value)
            onContinue()
        }
    }

    fun finishOnboarding(onFinish: () -> Unit) {
        viewModelScope.launch {
            completeOnboardingUseCase(_userData.value)
            clearCache()
            onFinish()
        }
    }

    private fun clearCache() {
        savedStateHandle.remove<String>(KEY_NAME)
        savedStateHandle.remove<String>(KEY_BIRTH_DATE)
        savedStateHandle.remove<String>(KEY_WEIGHT)
        savedStateHandle.remove<String>(KEY_HEIGHT)
        _userData.value = UserOnboardingData()
    }

    fun getMuscleGroups() = getMuscleGroupsUseCase()
    fun getExercisesByGroup(groupId: String) = getExercisesByGroupUseCase(groupId)
    fun getSuggestions() = getGoalSuggestionsUseCase()
}
