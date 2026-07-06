package com.example.evofit.presentation.ui.feature.workout.resume.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.ui.feature.workout.resume.state.WorkoutResumeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutResumeViewModel(
    private val workoutId: Long,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutResumeUiState())
    val uiState: StateFlow<WorkoutResumeUiState> = _uiState.asStateFlow()

    init {
        loadWorkout()
    }

    private fun loadWorkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getWorkoutByIdUseCase(workoutId).collect { workout ->
                workout?.let { w ->
                    _uiState.update {
                        it.copy(
                            workoutName = w.name,
                            totalExercises = w.exercises.size,
                            totalSets = w.exercises.sumOf { ex -> ex.sets.size },
                            formattedDate = w.date,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}
