package com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExercisesByGroupUseCase
import com.example.evofit.domain.usecase.GetExercisesByIdsUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.model.ExerciseSelectionUIModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.SelectExercisesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectExercisesViewModel(
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExercisesByIdsUseCase: GetExercisesByIdsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectExercisesUiState())
    val uiState = _uiState.asStateFlow()

    fun loadInitialData(muscleGroupIds: List<String>, editWorkoutId: String? = null) {
        if (_uiState.value.muscleGroupIds.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, muscleGroupIds = muscleGroupIds, editWorkoutId = editWorkoutId) }

            if (editWorkoutId != null) {
                val existingWorkout = getWorkoutByIdUseCase(editWorkoutId).first()
                existingWorkout?.let { workout ->
                    val exerciseIds = workout.exercises.map { it.exerciseId }
                    val exercisesData = getExercisesByIdsUseCase(exerciseIds)
                    
                    val groupedSelected = exercisesData.groupBy { it.muscleGroupId }
                        .mapValues { entry -> entry.value.map { it.id }.toSet() }
                        .filter { entry -> muscleGroupIds.contains(entry.key) } // Active Filtering

                    _uiState.update { 
                        it.copy(
                            workoutName = workout.name,
                            allSelectedExerciseIds = groupedSelected
                        )
                    }
                }
            } else if (muscleGroupIds.isNotEmpty()) {
                // Set default workout name from the first selected muscle group
                val muscleGroups = getMuscleGroupsUseCase()
                val firstGroupId = muscleGroupIds.first()
                val firstGroupName = muscleGroups.find { it.id.lowercase() == firstGroupId.lowercase() }?.name 
                    ?: firstGroupId.replaceFirstChar { it.uppercase() }
                
                _uiState.update { it.copy(workoutName = firstGroupName) }
            }

            loadCurrentGroupExercises()
        }
    }

    private suspend fun loadCurrentGroupExercises() {
        val state = _uiState.value
        val currentIndex = state.currentGroupIndex
        if (currentIndex !in state.muscleGroupIds.indices) return

        val muscleGroupId = state.muscleGroupIds[currentIndex]
        val muscleGroups = getMuscleGroupsUseCase()
        val group = muscleGroups.find { it.id.lowercase() == muscleGroupId.lowercase() }
        val groupName = group?.name ?: muscleGroupId.replaceFirstChar { it.uppercase() }

        val exercises = getExercisesByGroupUseCase(muscleGroupId)
        val uiExercises = exercises.map {
            ExerciseSelectionUIModel(it.id, it.name)
        }

        _uiState.update {
            it.copy(
                muscleGroupName = groupName,
                exercises = uiExercises,
                isLastGroup = currentIndex == state.muscleGroupIds.size - 1,
                isLoading = false
            )
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
        _uiState.update { state ->
            val currentGroupId = state.muscleGroupIds[state.currentGroupIndex]
            val currentSelected = state.allSelectedExerciseIds[currentGroupId] ?: emptySet()
            
            val newSelected = if (currentSelected.contains(exerciseId)) {
                currentSelected - exerciseId
            } else {
                currentSelected + exerciseId
            }
            
            val newAllSelected = state.allSelectedExerciseIds.toMutableMap().apply {
                put(currentGroupId, newSelected)
            }
            
            state.copy(allSelectedExerciseIds = newAllSelected)
        }
    }

    fun onContinueClick(onFinished: (List<String>, String, String?) -> Unit) {
        val state = _uiState.value
        if (state.isLastGroup) {
            val allIds = state.allSelectedExerciseIds.values.flatten().distinct()
            onFinished(allIds, state.workoutName, state.editWorkoutId)
        } else {
            _uiState.update { it.copy(currentGroupIndex = it.currentGroupIndex + 1, isLoading = true) }
            viewModelScope.launch { loadCurrentGroupExercises() }
        }
    }

    fun onBackPressed(onBackToGroupSelection: () -> Unit) {
        val state = _uiState.value
        if (state.currentGroupIndex > 0) {
            _uiState.update { it.copy(currentGroupIndex = it.currentGroupIndex - 1, isLoading = true) }
            viewModelScope.launch { loadCurrentGroupExercises() }
        } else {
            onBackToGroupSelection()
        }
    }
}
