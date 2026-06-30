package com.example.evofit.presentation.ui.feature.workout.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.presentation.ui.feature.workout.components.ExerciseSelectionUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SelectExercisesUiState(
    val muscleGroupName: String = "",
    val exercises: List<ExerciseSelectionUIModel> = emptyList(),
    val isLoading: Boolean = false
)

class SelectExercisesViewModel(
    private val getExerciseDataUseCase: GetExerciseDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectExercisesUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedExerciseIds = mutableStateListOf<String>()
    val selectedExerciseIds: List<String> get() = _selectedExerciseIds

    fun loadExercises(muscleGroupId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Aqui buscamos os grupos musculares para encontrar o nome correto
            val muscleGroups = getExerciseDataUseCase.getMuscleGroups()
            val group = muscleGroups.find { it.id.lowercase() == muscleGroupId.lowercase() }
            val groupName = group?.name ?: muscleGroupId.replaceFirstChar { it.uppercase() }

            val exercises = getExerciseDataUseCase.getExercisesByGroup(muscleGroupId)
            val uiExercises = exercises.map { 
                ExerciseSelectionUIModel(it.id, it.name)
            }

            _uiState.update { 
                it.copy(
                    muscleGroupName = groupName,
                    exercises = uiExercises,
                    isLoading = false
                )
            }
        }
    }

    fun toggleExerciseSelection(exerciseId: String) {
        if (_selectedExerciseIds.contains(exerciseId)) {
            _selectedExerciseIds.remove(exerciseId)
        } else {
            _selectedExerciseIds.add(exerciseId)
        }
    }
}
