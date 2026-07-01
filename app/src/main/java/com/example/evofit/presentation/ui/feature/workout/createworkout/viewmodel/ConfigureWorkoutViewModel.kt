package com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.data.model.MuscleGroupType
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.SaveWorkoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ConfigureWorkoutUiState(
    val workoutName: String = "",
    val exerciseConfigs: List<ExerciseConfigState> = emptyList(),
    val muscleGroupType: MuscleGroupType? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

data class ExerciseConfigState(
    val exerciseId: String,
    val name: String,
    val muscleGroupId: String,
    val sets: List<SetState> = listOf(SetState(1, 20.0, 10))
)

data class SetState(
    val setNumber: Int,
    val weight: Double,
    val reps: Int
)

class ConfigureWorkoutViewModel(
    private val getExerciseDataUseCase: GetExerciseDataUseCase,
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigureWorkoutUiState())
    val uiState = _uiState.asStateFlow()

    fun loadExercises(exerciseIds: List<String>, workoutName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, workoutName = workoutName) }
            val selectedExercises = getExerciseDataUseCase.getExercisesByIds(exerciseIds)
            val muscleGroupType = selectedExercises.firstOrNull()?.let { first ->
                getExerciseDataUseCase.getMuscleGroups().find { it.id == first.muscleGroupId }?.type
            }

            val configs = selectedExercises.map { exercise ->
                ExerciseConfigState(
                    exerciseId = exercise.id,
                    name = exercise.name,
                    muscleGroupId = exercise.muscleGroupId
                )
            }

            _uiState.update {
                it.copy(
                    exerciseConfigs = configs,
                    muscleGroupType = muscleGroupType,
                    isLoading = false
                )
            }
        }
    }

    fun addSet(exerciseId: String) {
        _uiState.update { state ->
            val updatedConfigs = state.exerciseConfigs.map { config ->
                if (config.exerciseId == exerciseId) {
                    val nextNumber = config.sets.size + 1
                    val lastSet = config.sets.lastOrNull()
                    config.copy(
                        sets = config.sets + SetState(
                            setNumber = nextNumber,
                            weight = lastSet?.weight ?: 20.0,
                            reps = lastSet?.reps ?: 10
                        )
                    )
                } else config
            }
            state.copy(exerciseConfigs = updatedConfigs)
        }
    }

    fun removeSet(exerciseId: String, setIndex: Int) {
        _uiState.update { state ->
            val updatedConfigs = state.exerciseConfigs.map { config ->
                if (config.exerciseId == exerciseId && config.sets.size > 1) {
                    val newSets = config.sets.toMutableList().apply { removeAt(setIndex) }
                        .mapIndexed { index, setState -> setState.copy(setNumber = index + 1) }
                    config.copy(sets = newSets)
                } else config
            }
            state.copy(exerciseConfigs = updatedConfigs)
        }
    }

    fun updateSet(exerciseId: String, setIndex: Int, weight: Double, reps: Int) {
        _uiState.update { state ->
            val updatedConfigs = state.exerciseConfigs.map { config ->
                if (config.exerciseId == exerciseId) {
                    val newSets = config.sets.toMutableList().apply {
                        this[setIndex] = this[setIndex].copy(weight = weight, reps = reps)
                    }
                    config.copy(sets = newSets)
                } else config
            }
            state.copy(exerciseConfigs = updatedConfigs)
        }
    }

    fun saveWorkout() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val workoutExercises = currentState.exerciseConfigs.map { config ->
                WorkoutExercise(
                    exerciseId = config.exerciseId,
                    sets = config.sets.map { set ->
                        ExerciseSet(
                            setNumber = set.setNumber,
                            reps = set.reps,
                            load = set.weight
                        )
                    }
                )
            }

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            val workout = Workout(
                userId = getUserIdUseCase() ?: "default_user",
                name = currentState.workoutName,
                muscleGroupId = currentState.exerciseConfigs.firstOrNull()?.muscleGroupId ?: "",
                date = dateFormat.format(Date()),
                exercises = workoutExercises
            )

            saveWorkoutUseCase(workout)
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}
