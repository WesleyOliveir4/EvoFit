package com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.core.common.AppConstants
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.domain.usecase.SaveWorkoutDoneUseCase
import com.example.evofit.domain.usecase.SaveWorkoutUseCase
import com.example.evofit.domain.repository.WorkoutSessionRepository
import com.example.evofit.presentation.mapper.DateMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class WorkoutStartUiState(
    val workoutTitle: String = "",
    val exercises: List<ExerciseProgressState> = emptyList(),
    val isLoading: Boolean = true,
    val elapsedTime: String = "00:00:00",
    val showFinishDialog: Boolean = false,
    val workoutCompleted: Boolean = false
)

data class ExerciseProgressState(
    val workoutExerciseId: Long,
    val exerciseId: String,
    val name: String,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val sets: List<SetProgressState>
)

data class SetProgressState(
    val setNumber: Int,
    val weight: Double,
    val reps: Int,
    val time: Int? = null,
    val distance: Double? = null,
    val isDone: Boolean = false
)

class WorkoutStartViewModel(
    private val workoutId: Int,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExerciseDataUseCase: GetExerciseDataUseCase,
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
    private val saveWorkoutDoneUseCase: SaveWorkoutDoneUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val sessionRepository: WorkoutSessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutStartUiState())
    val uiState: StateFlow<WorkoutStartUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var workoutDomain: Workout? = null

    init {
        loadWorkout()
        startOrResumeTimer()
    }

    private fun loadWorkout() {
        viewModelScope.launch {
            getWorkoutByIdUseCase(workoutId.toLong()).collect { workout ->
                workoutDomain = workout
                workout?.let { w ->
                    val groupName = w.muscleGroup?.name ?: ""
                    
                    val exerciseIds = w.exercises.map { it.exerciseId }
                    val exerciseDataMap = getExerciseDataUseCase.getExercisesByIds(exerciseIds)
                        .associateBy { it.id }

                    val exercises = w.exercises.map { workoutExercise ->
                        val exerciseInfo = exerciseDataMap[workoutExercise.exerciseId]

                        ExerciseProgressState(
                            workoutExerciseId = workoutExercise.id,
                            exerciseId = workoutExercise.exerciseId,
                            name = exerciseInfo?.name ?: "",
                            unit = workoutExercise.sets.firstOrNull()?.unit ?: MeasurementUnit.WEIGHT,
                            sets = workoutExercise.sets.mapIndexed { index, set ->
                                SetProgressState(
                                    setNumber = index + 1,
                                    weight = set.load,
                                    reps = set.reps,
                                    time = set.time,
                                    distance = set.distance
                                )
                            }
                        )
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            workoutTitle = w.name.ifEmpty { groupName },
                            exercises = exercises,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun startOrResumeTimer() {
        val now = System.currentTimeMillis()
        val startTime = sessionRepository.getSessionStartTime(workoutId) ?: run {
            sessionRepository.startSession(workoutId, now)
            now
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
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

    fun toggleSetDone(workoutExerciseId: Long, setNumber: Int) {
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
    }

    fun onFinishClick() {
        _uiState.update { it.copy(showFinishDialog = true) }
    }

    fun onDismissFinishDialog() {
        _uiState.update { it.copy(showFinishDialog = false) }
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
                    WorkoutExercise(
                        id = exercise.workoutExerciseId,
                        exerciseId = exercise.exerciseId,
                        sets = doneSets
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
            sessionRepository.clearSession()
            _uiState.update { it.copy(showFinishDialog = false, workoutCompleted = true) }
        }
    }

    companion object {
        private const val DEFAULT_EXERCISE_NAME = "Exercício"
    }
}
