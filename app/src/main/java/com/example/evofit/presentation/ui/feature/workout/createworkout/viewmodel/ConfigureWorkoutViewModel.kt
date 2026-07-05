package com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.core.common.AppConstants
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.SaveWorkoutUseCase
import com.example.evofit.presentation.mapper.DateMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

data class ConfigureWorkoutUiState(
    val workoutName: String = "",
    val exerciseConfigs: List<ExerciseConfigState> = emptyList(),
    val muscleGroupType: MuscleGroupType? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val savedWorkoutId: Long? = null
)

data class ExerciseConfigState(
    val exerciseId: String,
    val name: String,
    val muscleGroupId: String,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
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
            val muscleGroups = getExerciseDataUseCase.getMuscleGroups()
            val muscleGroupType = selectedExercises.firstOrNull()?.let { first ->
                muscleGroups.find { it.id == first.muscleGroupId }?.type
            }

            val configs = selectedExercises.map { exercise ->
                val defaultWeight = when (exercise.unit) {
                    MeasurementUnit.DISTANCE -> 1.0
                    else -> 20.0
                }
                val defaultReps = when (exercise.unit) {
                    MeasurementUnit.DISTANCE -> 5
                    MeasurementUnit.TIME -> 1
                    else -> 10
                }
                ExerciseConfigState(
                    exerciseId = exercise.id,
                    name = exercise.name,
                    muscleGroupId = exercise.muscleGroupId,
                    unit = exercise.unit,
                    sets = listOf(SetState(1, defaultWeight, defaultReps))
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
                    val defaultWeight = when (config.unit) {
                        MeasurementUnit.DISTANCE -> 1.0
                        else -> 20.0
                    }
                    val defaultReps = when (config.unit) {
                        MeasurementUnit.DISTANCE -> 5
                        MeasurementUnit.TIME -> 1
                        else -> 10
                    }
                    config.copy(
                        sets = config.sets + SetState(
                            setNumber = nextNumber,
                            weight = lastSet?.weight ?: defaultWeight,
                            reps = lastSet?.reps ?: defaultReps
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

            val muscleGroups = getExerciseDataUseCase.getMuscleGroups()
            val firstConfig = currentState.exerciseConfigs.firstOrNull()
            val muscleGroup = muscleGroups.find { it.id == firstConfig?.muscleGroupId }

            val workoutExercises = currentState.exerciseConfigs.map { config ->
                WorkoutExercise(
                    exerciseId = config.exerciseId,
                    sets = config.sets.map { set ->
                        when (config.unit) {
                            MeasurementUnit.DISTANCE -> {
                                ExerciseSet(
                                    exerciseName = config.name,
                                    setNumber = set.setNumber,
                                    reps = 0,
                                    load = 0.0,
                                    unit = config.unit,
                                    distance = set.weight,
                                    time = set.reps
                                )
                            }
                            MeasurementUnit.TIME -> {
                                ExerciseSet(
                                    exerciseName = config.name,
                                    setNumber = set.setNumber,
                                    reps = 0,
                                    load = 0.0,
                                    unit = config.unit,
                                    time = set.reps
                                )
                            }
                            MeasurementUnit.REPS -> {
                                ExerciseSet(
                                    exerciseName = config.name,
                                    setNumber = set.setNumber,
                                    reps = set.reps,
                                    load = 0.0,
                                    unit = config.unit
                                )
                            }
                            MeasurementUnit.WEIGHT -> {
                                ExerciseSet(
                                    exerciseName = config.name,
                                    setNumber = set.setNumber,
                                    reps = set.reps,
                                    load = set.weight,
                                    unit = config.unit
                                )
                            }
                        }
                    }
                )
            }

            val workout = Workout(
                userId = getUserIdUseCase() ?: AppConstants.DEFAULT_USER_ID,
                name = currentState.workoutName,
                muscleGroupId = firstConfig?.muscleGroupId ?: "",
                muscleGroup = muscleGroup,
                date = DateMapper.formatDate(Date()),
                exercises = workoutExercises
            )

            val workoutId = saveWorkoutUseCase(workout)
            _uiState.update { 
                it.copy(
                    isLoading = false, 
                    isSaved = true,
                    savedWorkoutId = workoutId
                ) 
            }
        }
    }
}
