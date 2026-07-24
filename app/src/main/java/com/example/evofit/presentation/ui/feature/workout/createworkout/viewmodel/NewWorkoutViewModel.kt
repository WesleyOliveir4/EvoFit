package com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExercisesByIdsUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.mapper.toItem
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.NewWorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewWorkoutViewModel(
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExercisesByIdsUseCase: GetExercisesByIdsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewWorkoutUiState())
    val uiState = _uiState.asStateFlow()

    fun loadMuscleGroups(editWorkoutId: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, editWorkoutId = editWorkoutId) }
            val groups = getMuscleGroupsUseCase()
            
            var initialSelected = emptySet<String>()
            if (editWorkoutId != null) {
                val workout = getWorkoutByIdUseCase(editWorkoutId).first()
                workout?.let { w ->
                    val exerciseIds = w.exercises.map { it.exerciseId }
                    val exercisesData = getExercisesByIdsUseCase(exerciseIds)
                    initialSelected = exercisesData.map { it.muscleGroupId }.toSet()
                }
            }

            _uiState.update {
                it.copy(
                    muscleGroups = groups.map { group -> group.toItem() },
                    selectedMuscleGroupIds = initialSelected,
                    isLoading = false
                )
            }
        }
    }

    fun toggleMuscleGroupSelection(groupId: String) {
        _uiState.update { state ->
            val newSelection = if (state.selectedMuscleGroupIds.contains(groupId)) {
                state.selectedMuscleGroupIds - groupId
            } else {
                state.selectedMuscleGroupIds + groupId
            }
            state.copy(selectedMuscleGroupIds = newSelection)
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
