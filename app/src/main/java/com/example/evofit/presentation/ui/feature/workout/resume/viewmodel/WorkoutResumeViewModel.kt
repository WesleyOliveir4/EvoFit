package com.example.evofit.presentation.ui.feature.workout.resume.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetWorkoutDoneByIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.ui.feature.workout.resume.state.WorkoutResumeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutResumeViewModel(
    private val workoutId: Long? = null,
    private val workoutDoneId: Long? = null,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getWorkoutDoneByIdUseCase: GetWorkoutDoneByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutResumeUiState())
    val uiState: StateFlow<WorkoutResumeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            if (workoutDoneId != null) {
                val workoutDone = getWorkoutDoneByIdUseCase(workoutDoneId)
                workoutDone?.let { wd ->
                    _uiState.update {
                        it.copy(
                            workoutName = wd.name,
                            totalExercises = wd.exercises.size,
                            totalSets = wd.exercises.sumOf { ex -> ex.sets.size },
                            completedSets = wd.exercises.sumOf { ex -> ex.sets.size }, 
                            duration = wd.time,
                            formattedDate = wd.date,
                            isLoading = false,
                            isWorkoutDone = true
                        )
                    }
                }
            } else if (workoutId != null) {
                getWorkoutByIdUseCase(workoutId).collect { workout ->
                    workout?.let { w ->
                        _uiState.update {
                            it.copy(
                                workoutName = w.name,
                                totalExercises = w.exercises.size,
                                totalSets = w.exercises.sumOf { ex -> ex.sets.size },
                                formattedDate = w.date,
                                isLoading = false,
                                isWorkoutDone = false
                            )
                        }
                    }
                }
            }
        }
    }
}
