package com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.presentation.mapper.toItem
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.NewWorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewWorkoutViewModel(
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewWorkoutUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMuscleGroups()
    }

    private fun loadMuscleGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val groups = getMuscleGroupsUseCase()
            _uiState.update {
                it.copy(
                    muscleGroups = groups.map { group -> group.toItem() },
                    isLoading = false
                )
            }
        }
    }
}