package com.example.evofit.presentation.ui.feature.workout.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.data.model.ExerciseModel
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
import java.util.Date

data class ConfigureWorkoutUiState(
    val selectedExercises: List<ExerciseModel> = emptyList(),
    val workoutName: String = "",
    val muscleGroupType: MuscleGroupType? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

data class ExerciseConfigState(
    val exerciseId: String,
    val name: String,
    val sets: MutableList<SetState> = mutableStateListOf(SetState(1, 20.0, 10))
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

    private val _exerciseConfigs = mutableStateListOf<ExerciseConfigState>()
    val exerciseConfigs: List<ExerciseConfigState> get() = _exerciseConfigs

    fun loadExercises(exerciseIds: List<String>, workoutName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, workoutName = workoutName) }
            
            val allMuscleGroups = getExerciseDataUseCase.getMuscleGroups()
            val allExercises = mutableListOf<ExerciseModel>()
            
            allMuscleGroups.forEach { group ->
                allExercises.addAll(getExerciseDataUseCase.getExercisesByGroup(group.id))
            }

            val selected = allExercises.filter { it.id in exerciseIds }
            
            val muscleGroupType = selected.firstOrNull()?.let { firstExercise ->
                allMuscleGroups.find { it.id == firstExercise.muscleGroupId }?.type
            }

            _exerciseConfigs.clear()
            selected.forEach { exercise ->
                _exerciseConfigs.add(
                    ExerciseConfigState(
                        exerciseId = exercise.id,
                        name = exercise.name
                    )
                )
            }

            _uiState.update { 
                it.copy(
                    selectedExercises = selected,
                    muscleGroupType = muscleGroupType,
                    isLoading = false
                )
            }
        }
    }

    fun addSet(exerciseId: String) {
        val config = _exerciseConfigs.find { it.exerciseId == exerciseId }
        config?.let {
            val nextSetNumber = it.sets.size + 1
            val lastSet = it.sets.lastOrNull()
            it.sets.add(SetState(nextSetNumber, lastSet?.weight ?: 20.0, lastSet?.reps ?: 10))
        }
    }

    fun removeSet(exerciseId: String, setIndex: Int) {
        val config = _exerciseConfigs.find { it.exerciseId == exerciseId }
        if (config != null && config.sets.size > 1) {
            config.sets.removeAt(setIndex)
            val newSets = config.sets.mapIndexed { index, setState ->
                setState.copy(setNumber = index + 1)
            }
            config.sets.clear()
            config.sets.addAll(newSets)
        }
    }

    fun updateSet(exerciseId: String, setIndex: Int, weight: Double, reps: Int) {
        val config = _exerciseConfigs.find { it.exerciseId == exerciseId }
        config?.let {
            it.sets[setIndex] = it.sets[setIndex].copy(reps = reps, weight = weight)
        }
    }

    fun saveWorkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val workoutExercises = _exerciseConfigs.map { config ->
                WorkoutExercise(
                    exerciseId = config.exerciseId,
                    sets = config.sets.map { 
                        ExerciseSet(
                            setNumber = it.setNumber,
                            reps = it.reps,
                            load = it.weight
                        )
                    }
                )
            }

            val userId = getUserIdUseCase() ?: "default_user"

            val workout = Workout(
                userId = userId,
                name = _uiState.value.workoutName,
                muscleGroupId = _uiState.value.selectedExercises.firstOrNull()?.muscleGroupId ?: "",
                date = Date().time,
                isCompleted = false,
                exercises = workoutExercises
            )

            saveWorkoutUseCase(workout)
            
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}
