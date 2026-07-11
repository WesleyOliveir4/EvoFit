package com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.model.ExerciseSelectionUIModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.SelectExercisesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectExercisesViewModel(
    private val getExerciseDataUseCase: GetExerciseDataUseCase,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectExercisesUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedExerciseIds = mutableStateListOf<String>()
    val selectedExerciseIds: List<String> get() = _selectedExerciseIds

    fun loadExercises(muscleGroupId: String, editWorkoutId: Long? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, editWorkoutId = editWorkoutId) }

            val muscleGroups = getExerciseDataUseCase.getMuscleGroups()
            val group = muscleGroups.find { it.id.lowercase() == muscleGroupId.lowercase() }
            val groupName = group?.name ?: muscleGroupId.replaceFirstChar { it.uppercase() }

            val exercises = getExerciseDataUseCase.getExercisesByGroup(muscleGroupId)
            val uiExercises = exercises.map {
                ExerciseSelectionUIModel(it.id, it.name)
            }

            val existingWorkout = editWorkoutId?.let { getWorkoutByIdUseCase(it).first() }
            _selectedExerciseIds.clear()
            existingWorkout?.let { workout ->
                _selectedExerciseIds.addAll(workout.exercises.map { it.exerciseId })
            }

            _uiState.update {
                it.copy(
                    muscleGroupName = groupName,
                    workoutName = existingWorkout?.name ?: groupName,
                    exercises = uiExercises,
                    isLoading = false
                )
            }
        }
    }

    fun startEditingName() {
        _uiState.update { it.copy(isEditingName = true, tempWorkoutName = it.workoutName) }
    }

    fun cancelEditingName() {
        _uiState.update { it.copy(isEditingName = false, tempWorkoutName = "") }
    }

    fun confirmEditingName() {
        _uiState.update {
            it.copy(
                workoutName = it.tempWorkoutName,
                isEditingName = false,
                tempWorkoutName = ""
            )
        }
    }

    fun updateTempName(newName: String) {
        _uiState.update { it.copy(tempWorkoutName = newName) }
    }

    fun toggleExerciseSelection(exerciseId: String) {
        if (_selectedExerciseIds.contains(exerciseId)) {
            _selectedExerciseIds.remove(exerciseId)
        } else {
            _selectedExerciseIds.add(exerciseId)
        }
    }

    fun onBackPressed(onProceed: () -> Unit) {
        if (_uiState.value.editWorkoutId != null) {
            _uiState.update { it.copy(showCancelEditDialog = true) }
        } else {
            onProceed()
        }
    }

    fun onConfirmCancelEdit(onProceed: () -> Unit) {
        _uiState.update { it.copy(showCancelEditDialog = false) }
        onProceed()
    }

    fun onDismissCancelEditDialog() {
        _uiState.update { it.copy(showCancelEditDialog = false) }
    }
}
