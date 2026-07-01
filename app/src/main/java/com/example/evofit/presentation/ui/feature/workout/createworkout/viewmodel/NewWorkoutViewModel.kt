package com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.NewWorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewWorkoutViewModel(
    private val getExerciseDataUseCase: GetExerciseDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewWorkoutUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMuscleGroups()
    }

    private fun loadMuscleGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val groups = getExerciseDataUseCase.getMuscleGroups()
            _uiState.update {
                it.copy(
                    muscleGroups = groups,
                    isLoading = false
                )
            }
        }
    }
}