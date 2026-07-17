package com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.core.common.AppConstants
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.usecase.GetExercisesByIdsUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.domain.usecase.SaveWorkoutUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutUseCase
import com.example.evofit.core.common.DateMapper
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.ConfigureWorkoutUiState
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.ExerciseConfigState
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.SetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class ConfigureWorkoutViewModel(
    private val getExercisesByIdsUseCase: GetExercisesByIdsUseCase,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val updateWorkoutUseCase: UpdateWorkoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigureWorkoutUiState())
    val uiState = _uiState.asStateFlow()
    private var originalOrderIndex: Int = 0
    private var originalDate: String = DateMapper.formatDate(Date())

    fun loadExercises(exerciseIds: List<String>, editWorkoutId: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, editWorkoutId = editWorkoutId) }
            val selectedExercises = getExercisesByIdsUseCase(exerciseIds)
            val muscleGroups = getMuscleGroupsUseCase()
            val muscleGroupType = selectedExercises.firstOrNull()?.let { first ->
                muscleGroups.find { it.id == first.muscleGroupId }?.type
            }

            val existingWorkout = editWorkoutId?.let { getWorkoutByIdUseCase(it).first() }
            val existingExercisesById = existingWorkout?.exercises?.associateBy { it.exerciseId } ?: emptyMap()
            existingWorkout?.let {
                originalOrderIndex = it.orderIndex
                originalDate = it.date
            }

            val configs = selectedExercises.map { exercise ->
                val existing = existingExercisesById[exercise.id]
                if (existing != null) {
                    ExerciseConfigState(
                        exerciseId = exercise.id,
                        name = exercise.name,
                        muscleGroupId = exercise.muscleGroupId,
                        unit = exercise.unit,
                        sets = existing.sets.mapIndexed { index, set -> set.toSetState(index + 1) }
                    )
                } else {
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

    private fun ExerciseSet.toSetState(setNumber: Int): SetState {
        return when (unit) {
            MeasurementUnit.DISTANCE -> SetState(setNumber, weight = distance ?: 0.0, reps = time ?: 0)
            MeasurementUnit.TIME -> SetState(setNumber, weight = 0.0, reps = time ?: 0)
            MeasurementUnit.REPS -> SetState(setNumber, weight = 0.0, reps = reps)
            MeasurementUnit.WEIGHT -> SetState(setNumber, weight = load, reps = reps)
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

    fun saveWorkout(workoutName: String) {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val (muscleGroup, workoutExercises) = buildMuscleGroupAndExercises(currentState)

            val workout = Workout(
                userId = getUserIdUseCase() ?: AppConstants.DEFAULT_USER_ID,
                name = workoutName,
                muscleGroupId = muscleGroup?.id ?: currentState.exerciseConfigs.firstOrNull()?.muscleGroupId.orEmpty(),
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


    fun saveEditedWorkout(workoutName: String) {
        val currentState = _uiState.value
        val editWorkoutId = currentState.editWorkoutId ?: return
        if (currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val (muscleGroup, workoutExercises) = buildMuscleGroupAndExercises(currentState)

            val workout = Workout(
                id = editWorkoutId,
                userId = getUserIdUseCase() ?: AppConstants.DEFAULT_USER_ID,
                name = workoutName,
                muscleGroupId = muscleGroup?.id ?: currentState.exerciseConfigs.firstOrNull()?.muscleGroupId.orEmpty(),
                muscleGroup = muscleGroup,
                date = originalDate,
                exercises = workoutExercises,
                orderIndex = originalOrderIndex
            )

            updateWorkoutUseCase(workout)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isSaved = true,
                    savedWorkoutId = editWorkoutId
                )
            }
        }
    }

    private suspend fun buildMuscleGroupAndExercises(
        currentState: ConfigureWorkoutUiState
    ): Pair<com.example.evofit.domain.model.MuscleGroup?, List<WorkoutExercise>> {
        val muscleGroups = getMuscleGroupsUseCase()
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

        return muscleGroup to workoutExercises
    }
}
