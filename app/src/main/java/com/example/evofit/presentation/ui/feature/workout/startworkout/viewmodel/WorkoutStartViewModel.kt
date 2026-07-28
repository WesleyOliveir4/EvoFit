package com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.core.common.AppConstants
import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.usecase.ClearWorkoutSessionUseCase
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCase
import com.example.evofit.domain.usecase.GetExercisesByIdsUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.domain.usecase.SaveWorkoutDoneUseCase
import com.example.evofit.domain.usecase.StartWorkoutSessionUseCase
import com.example.evofit.domain.usecase.UpdateCompletedSetsUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.core.common.DateMapper
import com.example.evofit.presentation.ui.feature.workout.startworkout.session.ExerciseProgressState
import com.example.evofit.presentation.ui.feature.workout.startworkout.session.SetProgressState
import com.example.evofit.presentation.ui.feature.workout.startworkout.session.WorkoutStartUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn

@OptIn(FlowPreview::class)
class WorkoutStartViewModel(
    private val workoutId: String,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExercisesByIdsUseCase: GetExercisesByIdsUseCase,
    private val saveWorkoutDoneUseCase: SaveWorkoutDoneUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getActiveWorkoutSessionUseCase: GetActiveWorkoutSessionUseCase,
    private val startWorkoutSessionUseCase: StartWorkoutSessionUseCase,
    private val updateCompletedSetsUseCase: UpdateCompletedSetsUseCase,
    private val clearWorkoutSessionUseCase: ClearWorkoutSessionUseCase,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutStartUiState())
    val uiState: StateFlow<WorkoutStartUiState> = _uiState.asStateFlow()

    private var workoutDomain: Workout? = null
    
     private val persistenceTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        loadWorkout()
        startOrResumeTimer()
        setupPersistence()
    }

    private fun setupPersistence() {
        persistenceTrigger
            .debounce(500)
            .onEach { persistCompletedSets() }
            .launchIn(viewModelScope)
    }

    private fun loadWorkout() {
        viewModelScope.launch {
            val workout = getWorkoutByIdUseCase(workoutId).first()
            val activeSession = getActiveWorkoutSessionUseCase().first()
            
            workoutDomain = workout
            workout?.let { w ->
                val groupName = w.muscleGroup?.name ?: ""
                val exerciseIds = w.exercises.map { it.exerciseId }
                val exerciseDataMap = getExercisesByIdsUseCase(exerciseIds).associateBy { it.id }
                val muscleGroups = getMuscleGroupsUseCase()
                val muscleGroupsMap = muscleGroups.associateBy { it.id }

                val completedSets = if (activeSession?.workout?.id == workoutId) {
                    activeSession.completedSets
                } else {
                    emptyList()
                }

                val exercises = w.exercises.map { workoutExercise ->
                    val exerciseInfo = exerciseDataMap[workoutExercise.exerciseId]
                    val mGroupId = exerciseInfo?.muscleGroupId ?: ""
                    val mGroupName = muscleGroupsMap[mGroupId]?.name ?: ""

                    ExerciseProgressState(
                        workoutExerciseId = workoutExercise.id,
                        exerciseId = workoutExercise.exerciseId,
                        name = exerciseInfo?.name ?: "",
                        muscleGroupName = mGroupName,
                        unit = workoutExercise.sets.firstOrNull()?.unit ?: MeasurementUnit.WEIGHT,
                        sets = workoutExercise.sets.map { set ->
                            SetProgressState(
                                setNumber = set.setNumber,
                                weight = set.load,
                                reps = set.reps,
                                time = set.time,
                                distance = set.distance,
                                isDone = completedSets.any {
                                    it.workoutExerciseId == workoutExercise.id && it.setNumber == set.setNumber
                                }
                            )
                        }
                    )
                }

                _uiState.update { it.copy(workoutTitle = w.name.ifEmpty { groupName }, exercises = exercises, isLoading = false) }
            }
        }
    }

    private fun startOrResumeTimer() {
        viewModelScope.launch {
            val activeSession = getActiveWorkoutSessionUseCase().first()
            val startTime = if (activeSession?.workout?.id == workoutId) {
                activeSession.startTime
            } else {
                val now = System.currentTimeMillis()
                startWorkoutSessionUseCase(workoutId, now)
                now
            }

            while (true) {
                val elapsed = System.currentTimeMillis() - startTime
                _uiState.update { it.copy(elapsedTime = formatElapsedTime(elapsed)) }
                delay(1000)
            }
        }
    }

    private fun formatElapsedTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))
        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

    fun toggleSetDone(workoutExerciseId: String, setNumber: Int) {
        _uiState.update { currentState ->
            val updatedExercises = currentState.exercises.map { exercise ->
                if (exercise.workoutExerciseId == workoutExerciseId) {
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
        
        // 2. Notifica o fluxo de persistência para salvar após o debounce
        persistenceTrigger.tryEmit(Unit)
    }

    private suspend fun persistCompletedSets() {
        val completedSets = _uiState.value.exercises.flatMap { exercise ->
            exercise.sets.filter { it.isDone }.map { set ->
                CompletedSet(workoutExerciseId = exercise.workoutExerciseId, setNumber = set.setNumber)
            }
        }
        updateCompletedSetsUseCase(completedSets)
    }

    fun onFinishClick() {
        _uiState.update { it.copy(showFinishDialog = true) }
    }

    fun onDismissFinishDialog() {
        _uiState.update { it.copy(showFinishDialog = false) }
    }

    fun onCancelWorkoutClick() {
        _uiState.update { it.copy(showCancelDialog = true) }
    }

    fun onDismissCancelDialog() {
        _uiState.update { it.copy(showCancelDialog = false) }
    }

    fun onConfirmCancelWorkout() {
        viewModelScope.launch {
            clearWorkoutSessionUseCase()
            _uiState.update { it.copy(showCancelDialog = false, workoutNotFinished = true) }
        }
    }

    fun onConfirmFinish() {
        viewModelScope.launch {
            val workout = workoutDomain ?: return@launch
            val userId = getUserIdUseCase() ?: AppConstants.DEFAULT_USER_ID

            val doneExercises = _uiState.value.exercises.mapNotNull { exercise ->
                val doneSets = exercise.sets.filter { it.isDone }.map { set ->
                    ExerciseSet(
                        exerciseName = exercise.name,
                        setNumber = set.setNumber,
                        reps = set.reps,
                        load = set.weight,
                        unit = exercise.unit,
                        time = set.time,
                        distance = set.distance
                    )
                }
                if (doneSets.isEmpty()) {
                    null
                } else {
                    val totalSetsPlanned = workout.exercises.find { it.id == exercise.workoutExerciseId }?.sets?.size ?: 0

                    WorkoutExercise(
                        id = exercise.workoutExerciseId,
                        exerciseId = exercise.exerciseId,
                        sets = doneSets,
                        totalSets = totalSetsPlanned
                    )
                }
            }

            val workoutDone = WorkoutDone(
                userId = userId,
                name = workout.name,
                muscleGroupId = workout.muscleGroupId,
                muscleGroup = workout.muscleGroup,
                date = DateMapper.formatDate(java.util.Date()),
                exercises = doneExercises,
                time = _uiState.value.elapsedTime
            )

            saveWorkoutDoneUseCase(userId, workoutDone)
            clearWorkoutSessionUseCase()

            val history = getWorkoutDoneHistoryUseCase(userId).first()
            val lastWorkoutDone = history.lastOrNull()

            _uiState.update { it.copy(
                showFinishDialog = false, 
                workoutCompleted = true,
                workoutDoneId = lastWorkoutDone?.id
            ) }
        }
    }
}
