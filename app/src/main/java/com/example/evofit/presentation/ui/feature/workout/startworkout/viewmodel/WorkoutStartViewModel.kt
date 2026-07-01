package com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class WorkoutStartUiState(
    val workoutTitle: String = "",
    val exercises: List<ExerciseProgressState> = emptyList(),
    val isLoading: Boolean = true
)

data class ExerciseProgressState(
    val id: String,
    val name: String,
    val sets: List<SetProgressState>
)

data class SetProgressState(
    val setNumber: Int,
    val weight: Double,
    val reps: Int,
    val isDone: Boolean = false
)

class WorkoutStartViewModel(
    private val workoutId: Int,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExerciseDataUseCase: GetExerciseDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutStartUiState())
    val uiState: StateFlow<WorkoutStartUiState> = _uiState.asStateFlow()

    init {
        loadWorkout()
    }

    private fun loadWorkout() {
        viewModelScope.launch {
            getWorkoutByIdUseCase(workoutId.toLong()).collect { workoutDomain ->
                workoutDomain?.let { workout ->
                    val groupName = workout.muscleGroup?.name ?: ""

                    val exercises = workout.exercises.map { workoutExercise ->
                        val exerciseInfo = getExerciseDataUseCase.getExercisesByGroup(workout.muscleGroupId)
                            .find { it.id == workoutExercise.exerciseId }

                        ExerciseProgressState(
                            id = workoutExercise.exerciseId,
                            name = exerciseInfo?.name ?: DEFAULT_EXERCISE_NAME,
                            sets = workoutExercise.sets.mapIndexed { index, set ->
                                SetProgressState(
                                    setNumber = index + 1,
                                    weight = set.load,
                                    reps = set.reps
                                )
                            }
                        )
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            workoutTitle = workout.name.ifEmpty { groupName },
                            exercises = exercises,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun toggleSetDone(exerciseId: String, setNumber: Int) {
        _uiState.update { currentState ->
            val updatedExercises = currentState.exercises.map { exercise ->
                if (exercise.id == exerciseId) {
                    val updatedSets = exercise.sets.map { set ->
                        if (set.setNumber == setNumber) {
                            set.copy(isDone = !set.isDone)
                        } else {
                            set
                        }
                    }
                    exercise.copy(sets = updatedSets)
                } else {
                    exercise
                }
            }
            currentState.copy(exercises = updatedExercises)
        }
    }

    companion object {
        private const val DEFAULT_EXERCISE_NAME = "Exercício"
    }
}
